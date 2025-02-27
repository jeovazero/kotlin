/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.expressions.builder

import kotlin.contracts.*
import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.fir.FirImplementationDetail
import org.jetbrains.kotlin.fir.builder.FirAnnotationContainerBuilder
import org.jetbrains.kotlin.fir.builder.FirBuilderDsl
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.expressions.FirThisReceiverExpression
import org.jetbrains.kotlin.fir.expressions.builder.FirExpressionBuilder
import org.jetbrains.kotlin.fir.expressions.builder.FirQualifiedAccessBuilder
import org.jetbrains.kotlin.fir.expressions.impl.FirNoReceiverExpression
import org.jetbrains.kotlin.fir.expressions.impl.FirThisReceiverExpressionImpl
import org.jetbrains.kotlin.fir.references.FirReference
import org.jetbrains.kotlin.fir.references.FirThisReference
import org.jetbrains.kotlin.fir.types.FirTypeProjection
import org.jetbrains.kotlin.fir.types.FirTypeRef
import org.jetbrains.kotlin.fir.types.impl.FirImplicitTypeRefImpl
import org.jetbrains.kotlin.fir.visitors.*

/*
 * This file was generated automatically
 * DO NOT MODIFY IT MANUALLY
 */

@FirBuilderDsl
class FirThisReceiverExpressionBuilder : FirQualifiedAccessBuilder, FirAnnotationContainerBuilder, FirExpressionBuilder {
    override var source: KtSourceElement? = null
    override var typeRef: FirTypeRef = FirImplicitTypeRefImpl(null)
    override val annotations: MutableList<FirAnnotation> = mutableListOf()
    override val typeArguments: MutableList<FirTypeProjection> = mutableListOf()
    lateinit var calleeReference: FirThisReference
    var isImplicit: Boolean = false

    override fun build(): FirThisReceiverExpression {
        return FirThisReceiverExpressionImpl(
            source,
            typeRef,
            annotations,
            typeArguments,
            calleeReference,
            isImplicit,
        )
    }


    @Deprecated("Modification of 'explicitReceiver' has no impact for FirThisReceiverExpressionBuilder", level = DeprecationLevel.HIDDEN)
    override var explicitReceiver: FirExpression?
        get() = throw IllegalStateException()
        set(_) {
            throw IllegalStateException()
        }

    @Deprecated("Modification of 'dispatchReceiver' has no impact for FirThisReceiverExpressionBuilder", level = DeprecationLevel.HIDDEN)
    override var dispatchReceiver: FirExpression
        get() = throw IllegalStateException()
        set(_) {
            throw IllegalStateException()
        }

    @Deprecated("Modification of 'extensionReceiver' has no impact for FirThisReceiverExpressionBuilder", level = DeprecationLevel.HIDDEN)
    override var extensionReceiver: FirExpression
        get() = throw IllegalStateException()
        set(_) {
            throw IllegalStateException()
        }
}

@OptIn(ExperimentalContracts::class)
inline fun buildThisReceiverExpression(init: FirThisReceiverExpressionBuilder.() -> Unit): FirThisReceiverExpression {
    contract {
        callsInPlace(init, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return FirThisReceiverExpressionBuilder().apply(init).build()
}
