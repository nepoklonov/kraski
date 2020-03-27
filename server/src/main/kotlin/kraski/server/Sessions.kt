package kraski.server

data class TestSession(
    val id: Int,
    val text: String,
    val counter: Int
)

data class UserSession(
    val level: Level
)

enum class Level(val lvl: Int, val password: String?) {
    SysAdmin(80, "administrationi247"),
    Admin(20, "there is no spoon"),
    Moderator(5, "kalevala-10"),
    JustSomeone(1, null);

    infix fun hasRights(other: Level) = lvl >= other.lvl
}

