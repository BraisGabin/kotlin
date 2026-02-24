/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.common.serialization

import org.jetbrains.kotlin.backend.common.serialization.proto.FileEntry
import org.jetbrains.kotlin.ir.IrFileEntry
import org.jetbrains.kotlin.ir.util.NaiveSourceBasedFileEntryImpl
import org.jetbrains.kotlin.backend.common.serialization.proto.FileEntry as ProtoFileEntry
import org.jetbrains.kotlin.backend.common.serialization.proto.IrFile as ProtoFile
import org.jetbrains.kotlin.backend.common.serialization.proto.IrInlinedFunctionBlock as ProtoInlinedFunctionBlock

class FileEntryDeserializer(private val irInterner: IrInterningService) {
    data class ProtoCacheKey(val libraryFile: IrLibraryFile, val protoIndex: Int)

    private val protoCache = mutableMapOf<ProtoCacheKey, FileEntry>()
    private val fileEntryCache = mutableMapOf<FileEntry, IrFileEntry>()

    fun fileEntry(libraryFile: IrLibraryFile, protoIndex: Int): IrFileEntry {
        val protoFileEntry = protoCache.getOrPut(ProtoCacheKey(libraryFile, protoIndex)) {
            libraryFile.fileEntry(protoIndex)!!
        }
        return fileEntryCache.getOrPut(protoFileEntry) {
            irInterner.fileEntry(libraryFile.deserializeFileEntry(protoFileEntry))
        }
    }

    fun fileEntry(libraryFile: IrLibraryFile, proto: ProtoInlinedFunctionBlock): IrFileEntry {
        val protoFileEntry = libraryFile.fileEntry(proto)
        return fileEntryCache.getOrPut(protoFileEntry) {
            irInterner.fileEntry(libraryFile.deserializeFileEntry(protoFileEntry))
        }
    }

    fun fileEntry(libraryFile: IrLibraryFile, proto: ProtoFile): IrFileEntry {
        val protoFileEntry = libraryFile.fileEntry(proto) {
            protoCache.getOrPut(ProtoCacheKey(libraryFile, it)) {
                libraryFile.fileEntry(it) ?: error("Invalid KLib: cannot read file entry by its index")
            }
        }
        return fileEntryCache.getOrPut(protoFileEntry) {
            irInterner.fileEntry(libraryFile.deserializeFileEntry(protoFileEntry))
        }
    }

    private fun IrLibraryFile.fileEntry(proto: ProtoInlinedFunctionBlock): FileEntry =
        if (proto.hasInlinedFunctionFileEntryId()) {
            protoCache.getOrPut(ProtoCacheKey(this, proto.inlinedFunctionFileEntryId)) {
                fileEntry(proto.inlinedFunctionFileEntryId) ?: error("Invalid KLib: cannot read file entry by its index")
            }
        } else {
            require(proto.hasInlinedFunctionFileEntry()) {
                "Invalid KLib: either fileEntry or fileEntryId must be present in serialized IrInlinedFunctionBlock"
            }
            proto.inlinedFunctionFileEntry
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