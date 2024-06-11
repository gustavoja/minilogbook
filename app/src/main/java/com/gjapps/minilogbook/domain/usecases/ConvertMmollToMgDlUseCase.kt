package com.gjapps.minilogbook.domain.usecases

interface ConvertMgDlToMmollUseCase {
    operator fun invoke(mmoll: Float): Float
}

class ConvertMgDlToMmollUseCaseImpl: ConvertMgDlToMmollUseCase {
    override operator fun invoke(mgdl: Float): Float {
        return mgdl / 18.0182f
    }
}