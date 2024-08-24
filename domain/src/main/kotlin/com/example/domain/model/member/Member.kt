package com.example.domain.model.member

data class Member(
    val id: MemberId = MemberId.generate(),
    val name: String,
    val email: String,
) {
    private var status: MemberStatus = MemberStatus.ACTIVE // 상태 필드는 private으로 설정

    // 상태를 외부에서 읽을 수 있는 메서드
    fun getStatus(): MemberStatus = status

    // Member를 활성화하는 메서드
    fun activate() {
        if (this.status == MemberStatus.INACTIVE) {
            this.status = MemberStatus.ACTIVE
        }
    }

    // Member를 비활성화하는 메서드
    fun deactivate() {
        if (this.status == MemberStatus.ACTIVE) {
            this.status = MemberStatus.INACTIVE
        }
    }
}
