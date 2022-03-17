/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.commonizer.core

import org.jetbrains.kotlin.commonizer.cir.*

interface CirTypeVisitor<D> {
    fun visit(type: CirType, data: D)
    fun visit(flexibleType: CirFlexibleType, data: D)
    fun visit(simpleType: CirSimpleType, data: D)
    fun visit(typeParameterType: CirTypeParameterType, data: D)
    fun visit(classOrTypeAliasType: CirClassOrTypeAliasType, data: D)
    fun visit(classType: CirClassType, data: D)
    fun visit(typeAliasType: CirTypeAliasType, data: D)
    fun visit(typeProjection: CirTypeProjection, data: D)
}

open class AbstractCirTypeVisitor<D> : CirTypeVisitor<D> {
    override fun visit(type: CirType, data: D) {
        return when (type) {
            is CirFlexibleType -> visit(type, data)
            is CirSimpleType -> visit(type, data)
        }
    }

    override fun visit(flexibleType: CirFlexibleType, data: D) {
        visit(flexibleType.lowerBound, data)
        visit(flexibleType.upperBound, data)
    }

    override fun visit(simpleType: CirSimpleType, data: D) {
        when (simpleType) {
            is CirTypeParameterType -> visit(simpleType, data)
            is CirClassOrTypeAliasType -> visit(simpleType, data)
        }
    }

    override fun visit(typeParameterType: CirTypeParameterType, data: D) {
    }

    override fun visit(classOrTypeAliasType: CirClassOrTypeAliasType, data: D) {
        when (classOrTypeAliasType) {
            is CirClassType -> visit(classOrTypeAliasType, data)
            is CirTypeAliasType -> visit(classOrTypeAliasType, data)
        }
    }

    override fun visit(classType: CirClassType, data: D) {
        classType.outerType?.let { visit(it, data) }
        classType.arguments.forEach { visit(it, data) }
    }

    override fun visit(typeAliasType: CirTypeAliasType, data: D) {
        visit(typeAliasType.underlyingType, data)
    }

    override fun visit(typeProjection: CirTypeProjection, data: D) {
        if (typeProjection is CirRegularTypeProjection)
            visit(typeProjection.type, data)
    }
}

open class CirTypeVisitorUnit : AbstractCirTypeVisitor<Unit>()

fun <T> CirType.accept(visitor: CirTypeVisitor<T>, data: T) {
    visitor.visit(this, data)
}

fun CirType.accept(visitor: CirTypeVisitorUnit) {
    visitor.visit(this, Unit)
}
