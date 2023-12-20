package com.bhaskardamayanthi.gossy.managers

object EmojiManager {
    val emojiList = listOf(
        "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "🥹",
        "☺️", "😊", "😇", "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘",
        "😗", "😙", "😚", "😋", "😛", "😝", "😜", "🤪", "🤨", "🧐",
        "🤓", "😎", "🤩", "🥳", "😏", "🤯", "😳", "🥵", "🥶", "😱",
        "🫣", "🤗", "🫡", "🤔", "🫢", "🤭",
        "🤫", "🤥", "😶", "😶‍🌫️", "😐", "😑", "😬", "🫨", "🫠", "🙄",
        "😯", "😦", "😧", "😮", "😲", "🥱", "😴", "🤤", "😪", "😵",
        "😵‍💫", "🫥", "🤐", "🤑", "🤠", "👻", "💀",
        "☠️", "👽", "👾", "🤖", "🎃", "😺", "😸", "😹", "😻", "😼",
        "😽", "🙀"
    )
    fun getEmoji(postion:Int): String {

        return emojiList[postion]
    }
}