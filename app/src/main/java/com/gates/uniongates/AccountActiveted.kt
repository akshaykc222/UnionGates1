package com.gates.uniongates

interface AccountActiveted {
    fun onAccountReceived(allowToOrder:Boolean)
    fun onError(error: String)
}