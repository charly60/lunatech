package core.dataBase

import org.scalatest.{Matchers, WordSpecLike}


class AirportQueriesSpec extends AirportQueries with WordSpecLike with Matchers {

  findAllAirport().length should be > 0

  val (lowAirportNumberCountry, highAirportNumberCountry) = getNumberOfAirportPerCountry()

  lowAirportNumberCountry should have size 10
  lowAirportNumberCountry.toString should be ("List((JE,1), (GI,1), (NF,1), (MO,1), (ZZ,1), (BL,1), (CW,1), (AD,1), (SH,1), (CC,1))")

  highAirportNumberCountry should have size 10
  highAirportNumberCountry.toString should be ("List((VE,592), (CO,700), (DE,703), (AR,713), (FR,789), (RU,920), (AU,1908), (CA,2454), (BR,3839), (US,21501))")


}
