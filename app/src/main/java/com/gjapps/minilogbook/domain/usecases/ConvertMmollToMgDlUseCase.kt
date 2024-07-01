package com.gjapps.minilogbook.domain.usecases

interface ConvertMmollToMgDlUseCase {
    operator fun invoke(mmoll: Float): Float
}

class ConvertMmollToMgDlUseCaseImpl: ConvertMmollToMgDlUseCase {
    override operator fun invoke(mmoll: Float): Float {
        return mmoll * 18.0182f
    }
}