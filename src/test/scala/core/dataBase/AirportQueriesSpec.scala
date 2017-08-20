package core.dataBase

import org.scalatest.{Matchers, WordSpecLike}


class AirportQueriesSpec extends AirportQueries with WordSpecLike with Matchers {

  findAllAirport().length should be > 0

  findAirportsByCountry("FR").length should be > 0

}
