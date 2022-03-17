/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.commonizer.core

import org.jetbrains.kotlin.commonizer.CommonizerSettings
import org.jetbrains.kotlin.commonizer.CommonizerTarget
import org.jetbrains.kotlin.commonizer.OptimisticNumberCommonizationEnabledKey
import org.jetbrains.kotlin.commonizer.allLeaves
import org.jetbrains.kotlin.commonizer.cir.*
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstanceOrNull

typealias RenderedType = String

private class UnsafeNumberAnnotation(val actualPlatformTypes: Map<String, RenderedType>) : CirAnnotation {
    override val type: CirClassType = UnsafeNumberAnnotation.type
    override val annotationValueArguments: Map<CirName, CirAnnotation> = emptyMap()

    override val constantValueArguments: Map<CirName, CirConstantValue> = mapOf(
        CirName.create("actualPlatformTypes") to CirConstantValue.ArrayValue(
            actualPlatformTypes.toSortedMap().map { (platform, type) ->
                CirConstantValue.StringValue("$platform: $type")
            }
        )
    )

    companion object {
        private val type = CirClassType.createInterned(
            classId = CirEntityId.create("kotlinx/cinterop/UnsafeNumber"),
            outerType = null,
            arguments = emptyList(),
            isMarkedNullable = false
        )
    }
}

abstract class UnsafeNumberAnnotationCreator<T : CirHasAnnotations>(
    private val settings: CommonizerSettings,
) {
    protected abstract fun getType(declaration: T): CirType

    fun createAnnotationIfNecessary(
        targets: List<CommonizerTarget>,
        inputDeclarations: List<T>,
        resultingType: CirType?
    ): CirAnnotation? {
        if (!shouldCreateAnnotation(resultingType, inputDeclarations))
            return null

        val inputTypes = inputDeclarations.map { declaration -> getType(declaration) }

        val actualPlatformTypes = mutableMapOf<String, RenderedType>()

        inputTypes.zip(targets).forEach { (cirClassType, target) ->
            target.allLeaves().forEach { leafCommonizerTarget ->
                actualPlatformTypes[leafCommonizerTarget.name] = CirTypeRendererForUnsafeNumberAnnotation.renderType(cirClassType)
            }
        }

        inputDeclarations.forEach { annotated ->
            val existingAnnotation = annotated.annotations.firstIsInstanceOrNull<UnsafeNumberAnnotation>()
            if (existingAnnotation != null) {
                actualPlatformTypes.putAll(existingAnnotation.actualPlatformTypes)
            }
        }

        if (actualPlatformTypes.values.distinct().size > 1) {
            return UnsafeNumberAnnotation(actualPlatformTypes)
        }

        return null
    }

    private fun shouldCreateAnnotation(
        commonizedType: CirType?,
        inputDeclarations: List<T>,
    ): Boolean {
        if (!settings.getSetting(OptimisticNumberCommonizationEnabledKey))
            return false

        val annotatedInputDeclarationPresent = inputDeclarations.any { declaration ->
            declaration.annotations.any { annotation -> annotation is UnsafeNumberAnnotation }
        }

        if (annotatedInputDeclarationPresent)
            return true

        if (commonizedType == null)
            return false

        var isMarkedTypeFound = false

        commonizedType.accept(object : CirTypeVisitorUnit() {
            override fun visit(classType: CirClassType, data: Unit) {
                classType.getAttachment<OptimisticNumbersTypeCommonizer.OptimisticCommonizationMarker>()?.let { isMarkedTypeFound = true }
                    ?: super.visit(classType, data)
            }
        })

        return isMarkedTypeFound
    }
}

class UnsafeNumberAnnotationCreatorForTypeAlias(
    settings: CommonizerSettings
) : UnsafeNumberAnnotationCreator<CirTypeAlias>(settings) {
    override fun getType(declaration: CirTypeAlias): CirType = declaration.underlyingType
}

class UnsafeNumberAnnotationCreatorForFunctionOrProperty(
    settings: CommonizerSettings
) : UnsafeNumberAnnotationCreator<CirFunctionOrProperty>(settings) {
    override fun getType(declaration: CirFunctionOrProperty): CirType = declaration.returnType
}
