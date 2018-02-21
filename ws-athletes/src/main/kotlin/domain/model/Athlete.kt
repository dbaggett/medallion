package domain.model

data class Athlete(val id: Long, val first: String, val last: String, val gender: Gender, val competitions: List<Competition>)