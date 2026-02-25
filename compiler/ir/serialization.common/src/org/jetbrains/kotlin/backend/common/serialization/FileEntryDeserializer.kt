/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.common.serialization

import org.jetbrains.kotlin.ir.IrFileEntry
import org.jetbrains.kotlin.ir.util.NaiveSourceBasedFileEntryImpl
import org.jetbrains.kotlin.backend.common.serialization.proto.FileEntry as ProtoFileEntry
import org.jetbrains.kotlin.backend.common.serialization.proto.IrFile as ProtoFile
import org.jetbrains.kotlin.backend.common.serialization.proto.IrInlinedFunctionBlock as ProtoInlinedFunctionBlock

class FileEntryDeserializer(private val irInterner: IrInterningService) {
    data class ProtoCacheKey(val libraryFile: IrLibraryFile, val protoIndex: Int)

    private val cache = mutableMapOf<ProtoCacheKey, IrFileEntry>()

    fun fileEntry(libraryFile: IrLibraryFile, protoIndex: Int): IrFileEntry {
        return cache.getOrPut(ProtoCacheKey(libraryFile, protoIndex)) {
            val protoFileEntry = libraryFile.fileEntry(protoIndex)!!
            irInterner.fileEntry(libraryFile.deserializeFileEntry(protoFileEntry))
        }
    }

    fun fileEntry(libraryFile: IrLibraryFile, proto: ProtoInlinedFunctionBlock): IrFileEntry {
        return if (proto.hasInlinedFunctionFileEntryId()) {
            cache.getOrPut(ProtoCacheKey(libraryFile, proto.inlinedFunctionFileEntryId)) {
                val protoFileEntry = libraryFile.fileEntry(proto.inlinedFunctionFileEntryId) ?: error("Invalid KLib: cannot read file entry by its index")
                irInterner.fileEntry(libraryFile.deserializeFileEntry(protoFileEntry))
            }
        } else {
            require(proto.hasInlinedFunctionFileEntry()) {
                "Invalid KLib: either fileEntry or fileEntryId must be present in serialized IrInlinedFunctionBlock"
            }
            irInterner.fileEntry(libraryFile.deserializeFileEntry(proto.inlinedFunctionFileEntry))
        }
    }

    fun fileEntry(libraryFile: IrLibraryFile, proto: ProtoFile): IrFileEntry {
        val deserializedFileEntry = if (proto.hasFileEntryId()) {
            val protoFileEntry = libraryFile.fileEntry(proto.fileEntryId) ?: error("Invalid KLib: cannot read file entry by its index")
            libraryFile.deserializeFileEntry(protoFileEntry)
        } else {
            require(proto.hasFileEntry()) {
                "Invalid KLib: either fileEntry or fileEntryId must be present"
            }
            libraryFile.deserializeFileEntry(proto.fileEntry)
        }
        return irInterner.fileEntry(deserializedFileEntry)
    }

    private fun IrLibraryFile.deserializeFileEntry(fileEntryProto: ProtoFileEntry): IrFileEntry {
        val lineStartOffsets: IntArray
        if (fileEntryProto.lineStartOffsetDeltaCount > 0) {
            lineStartOffsets = IntArray(fileEntryProto.lineStartOffsetDeltaCount)
            var offset = 0
            for ((index, delta) in fileEntryProto.lineStartOffsetDeltaList.withIndex()) {
                offset += delta
                lineStartOffsets[index] = offset
            }
        } else {
            lineStartOffsets = fileEntryProto.lineStartOffsetList.toIntArray()
        }

        val name = irInterner.string(deserializeFileEntryName(fileEntryProto))
        val file = NaiveSourceBasedFileEntryImpl(
            name = name,
            lineStartOffsets = lineStartOffsets,
            firstRelevantLineIndex = fileEntryProto.firstRelevantLineIndex
        )
        return irInterner.fileEntry(file)
    }
}