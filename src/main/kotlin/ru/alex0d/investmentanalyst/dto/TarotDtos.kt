package ru.alex0d.investmentanalyst.dto

data class TarotPredictionResponse(
    val cardName: String,
    val prediction: String
)

enum class TarotCard {
    THE_FOOL,
    THE_MAGICIAN,
    THE_HIGH_PRIESTESS,
    THE_EMPRESS,
    THE_EMPEROR,
    THE_HIEROPHANT,
    THE_LOVERS,
    THE_CHARIOT,
    JUSTICE,
    THE_HERMIT,
    WHEEL_OF_FORTUNE,
    STRENGTH,
    THE_HANGED_MAN,
    DEATH,
    TEMPERANCE,
    THE_DEVIL,
    THE_TOWER,
    THE_MOON,
    THE_SUN,
//    THE_STAR,
//    JUDGEMENT,
//    THE_WORLD
}

fun TarotCard.toRussianName() = when(this) {
    TarotCard.THE_FOOL -> "Дурак"
    TarotCard.THE_MAGICIAN -> "Маг"
    TarotCard.THE_HIGH_PRIESTESS -> "Жрица"
    TarotCard.THE_EMPRESS -> "Императрица"
    TarotCard.THE_EMPEROR -> "Император"
    TarotCard.THE_HIEROPHANT -> "Жрец"
    TarotCard.THE_LOVERS -> "Влюблённые"
    TarotCard.THE_CHARIOT -> "Колесница"
    TarotCard.JUSTICE -> "Правосудие"
    TarotCard.THE_HERMIT -> "Отшельник"
    TarotCard.WHEEL_OF_FORTUNE -> "Колесо фортуны"
    TarotCard.STRENGTH -> "Сила"
    TarotCard.THE_HANGED_MAN -> "Повешенный"
    TarotCard.DEATH -> "Смерть"
    TarotCard.TEMPERANCE -> "Умеренность"
    TarotCard.THE_DEVIL -> "Дьявол"
    TarotCard.THE_TOWER -> "Башня"
    TarotCard.THE_MOON -> "Луна"
    TarotCard.THE_SUN -> "Солнце"
//    TarotCard.THE_STAR -> "Звезда"
//    TarotCard.JUDGEMENT -> "Суд"
//    TarotCard.THE_WORLD -> "Мир"
}