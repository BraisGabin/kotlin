/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.lexer.KtTokens;
import org.jetbrains.kotlin.resolution.KtResolvableCall;

/**
 * Represents a callable reference expression using double colon syntax.
 *
 * <h3>Example:</h3>
 * <pre>{@code
 * fun foo() {}
 * fun main() {
 *     ::foo
 * //  ^___^
 * }
 * }</pre>
 */
public class KtCallableReferenceExpression extends KtExpressionImpl implements KtDoubleColonExpression, KtResolvableCall {
    public KtCallableReferenceExpression(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public KtSimpleNameExpression getCallableReference() {
        PsiElement psi = getDoubleColonTokenReference();
        while (psi != null) {
            if (psi instanceof KtSimpleNameExpression) {
                return (KtSimpleNameExpression) psi;
            }
            psi = psi.getNextSibling();
        }

        throw new IllegalStateException("Callable reference simple name shouldn't be parsed to null");
    }

    @Nullable
    @Override
    public PsiElement findColonColon() {
        return findChildByType(KtTokens.COLONCOLON);
    }

    /**
     * Returns the erroneous value argument list that may be present after the callable reference.
     * This syntax is invalid: {@code ::foo(args)}.
     *
     * @return the erroneous value argument list, or null if not present
     */
    @Nullable
    public KtValueArgumentList getErrorValueArgumentList() {
        PsiErrorElement errorElement = PsiTreeUtil.findChildOfType(this, PsiErrorElement.class);
        if (errorElement == null) return null;
        return PsiTreeUtil.findChildOfType(errorElement, KtValueArgumentList.class);
    }

    @Override
    public <R, D> R accept(@NotNull KtVisitor<R, D> visitor, D data) {
        return visitor.visitCallableReferenceExpression(this, data);
    }
}
