package core.dataBase

import org.scalatest.{Matchers, OptionValues, WordSpecLike}


class CountryQueriesSpec extends CountryQueries with WordSpecLike with Matchers {

  findByNameOrCode("France") should be (findByNameOrCode("FR"))

  findByNameOrCode("US") should not be (findByNameOrCode("UK"))

  val spain = findByNameOrCode("Spain")

  spain.isDefined should be (true)
  spain.get.code should be ("ES")
  spain.get.name should be ("Spain")
  spain.get.continent should be ("EU")
  spain.get.wikipediaLink should be ("http://en.wikipedia.org/wiki/Spain")

}
