package core.dataBase

import org.scalatest.{Matchers, WordSpecLike}


class RunwayQueriesSpec extends RunwaysQueries with WordSpecLike with Matchers{

  findAllRunways().size should be > 0

  val byRef = findByAirportRef(6537L)
  byRef should have size 2

}
