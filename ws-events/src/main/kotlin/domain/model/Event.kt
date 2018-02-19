package domain.model

data class Event(val id: Long, val name: EventType, val sport: Sport, val olympics: List<OlympicGame>)