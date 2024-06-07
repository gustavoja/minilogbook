package com.gjapps.minilogbook.domain.usecases

interface ConverMgDlToMmollUseCase {
    operator fun invoke(mmoll: Float): Float
}

class ConverMgDlToMmollUseCaseImpl: ConverMgDlToMmollUseCase {
    override operator fun invoke(mgdl: Float): Float {
        return mgdl / 18.0182f
    }
}