package domain.model

data class Medal(
        val id: Long,
        val rank: Rank,
        val athleteId: Long,
        val event: EventType,
        val sport: Sport,
        val year: Int
)