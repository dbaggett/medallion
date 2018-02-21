package domain.model

data class Competition(val eventType: EventType, val sport: Sport, val season: Season, val year: Int, val country: String)