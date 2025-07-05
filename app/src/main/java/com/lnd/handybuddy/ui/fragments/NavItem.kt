package com.lnd.handybuddy.ui.fragments

sealed class NavItem {
    object ProfileHeader : NavItem()
    data class SectionHeader(val title: String) : NavItem()
    data class Option(val title: String) : NavItem()
}