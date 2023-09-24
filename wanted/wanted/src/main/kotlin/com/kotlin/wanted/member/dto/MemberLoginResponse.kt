package com.kotlin.wanted.member.dto

data class MemberLoginResponse(val result: Boolean,val email : String, val token : String, val message : String)