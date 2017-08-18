package exoRoutes

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.scaladsl.{Keep, Sink}
import core.dataBase.{Airport, Runway}
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.Await


class RoutesHttpSpec extends RoutesHttp with WordSpecLike with Matchers with ScalatestRouteTest {

  val airport1 = Airport(id= 1,
    ident= "1",
    airportType= "type",
    name = "airport1",
    latitudeDeg = 0,
    longitudeDeg = 0,
    elevationFt = None,
    continent = "continent",
    isoCountry = "isoCountry1",
    isoRegion = "isoRegion1",
    municipality = None,
    scheduledService = "service1",
    gpsCode = None,
    iataCode = None,
    localCode = None,
    homeLink = None,
    wikipediaLink = None,
    keywords = None)

  val airport2 = Airport(id= 2,
    ident= "2",
    airportType= "type2",
    name = "airport2",
    latitudeDeg = 0,
    longitudeDeg = 0,
    elevationFt = None,
    continent = "continent",
    isoCountry = "isoCountry",
    isoRegion = "isoRegion2",
    municipality = None,
    scheduledService = "service2",
    gpsCode = None,
    iataCode = None,
    localCode = None,
    homeLink = None,
    wikipediaLink = None,
    keywords = None)

  val airport3 = Airport(id= 3,
    ident= "3",
    airportType= "type",
    name = "airport3",
    latitudeDeg = 0,
    longitudeDeg = 0,
    elevationFt = None,
    continent = "continent2",
    isoCountry = "isoCountry",
    isoRegion = "isoRegion3",
    municipality = None,
    scheduledService = "service3",
    gpsCode = None,
    iataCode = None,
    localCode = None,
    homeLink = None,
    wikipediaLink = None,
    keywords = None)

  val jsonAirport = airportsJsonify(List(airport1, airport2, airport3))

  runwaysJsonify(List.empty[Runway]) should be ("[ ]")

  jsonAirport should be (s"""[ { "airportType" : "type", "name" : "airport1", "runways" : [ ] },{ "airportType" : "type2", "name" : "airport2", "runways" : [ { "runway_id" : "232775", "lighted" : "0", "closed": "0" } ] },{ "airportType" : "type", "name" : "airport3", "runways" : [ { "runway_id" : "233754", "lighted" : "1", "closed": "0" } ] } ]""")

  getTypeOfRunwayPerCountry().toString() should be (s"""Map(CV -> List(ASP, UNK), MA -> List(ASP, CON, BIT, UNK), AO -> List(ASP, UNK, null), VN -> List(ASP, CON, UNK, null), IN -> List(ASP, CON, PEM, UNK, PAVED, paved, GRE, MAC, BIT, GVL, COM, asphalt, GRS, null, Bitumen, Dirt, Grass, grass/concrete, ASPHALT, Concrete), KW -> List(ASP, CON, PEM), ML -> List(LAT, UNK, GVL, ASP, Brown clay gravel), ID -> List(ASP, Compacted sand, Grass/Clay, ASPHALT, asphalt, Asphalt/Grass, PEM, CON, GRE, ASPH/CONC, GRS, Coral, Coral penetration, Gravel, Compacted coral and sand, Asphalt, GVL, null, Grass, Gravel/clay, Sand laterite, Grass/gravel, Asphalt concrete, Dirt/rock, Grass over gravel, Clay, Gravel/grass, Asphalt/Concrete, Gravel/dirt, Grass/dirt, Grass over rock, Grass/Moss, Clay/grass, Grass/clay, Graded earth, Gravel/soil, Sand/clay, Dirt/grass, Sod over hard clay, Sand/grass, Grass/sandy soil, Grass/red clay, Sod with gravel & sand, Rocky gravel, Grass over hard gravel, Hard clay, Grass/rolled earth, Hard mud, Rock/Gravel/Clay, Gravel/grass, First 410m of RWY 23 paved, Grass Dirt, grass), JE -> List(ASP), EG -> List(ASP, UNK, Asphalt), BG -> List(CON, ASP, GRE, PEM, concrete), SG -> List(ASP, GRS, null), SV -> List(ASP), TC -> List(ASP, UNK), TH -> List(CON, ASP), AT -> List(GRS, ASP, CLOSED, ASPHALT, GRASS, CON), GQ -> List(CON, UNK, ASP), BD -> List(PEM, ASP, CON), TR -> List(Concrete, CON, CONCRETE, ASP, Asphalt, concrete, PER), HT -> List(ASP), UM -> List(ASP, UNK), MY -> List(ASP, paved, GRE, null), RU -> List(CON, ASP, GRE, GRS, concrete, Ground, PEM, UNK, Asphalt, Concrete, grass, null, 'concrete', Grass), MH -> List(GRVL, ASP), NI -> List(ASP, CON), BZ -> List(ASP), KP -> List(CON, GRS, ASP), VE -> List(ASP, null, CONCRETE, UNK, CON), IL -> List(ASP, Asphalt, PEM), GD -> List(UNK, ASP), GI -> List(ASP), TN -> List(ASP, CON, PEM), DM -> List(ASP), PR -> List(ASPH-F, ASP, CON, ASPH-CONC, ASPH-G, WATER, TURF, ASPH), NF -> List(ASP), MO -> List(CON), TW -> List(ASP, PEM, CON, null, UNK), KN -> List(ASP), PH -> List(ASP, CON, COP, PEM, ASP/CON, null, Paved, dirt, BIT, Concrete), WF -> List(ASP, UNK), JO -> List(ASP, CON), ME -> List(ASP, GRS), ES -> List(ASP, CON, GRE, concrete, null, UNK, Asphalt, ASPHALT, GRS), AZ -> List(CON, ASP), MR -> List(ASP, MAC, BIT), SM -> List(), ZZ -> List(), BL -> List(), PK -> List(ASP, SAN, 'asphalt', PEM, BIT, UNK, GRE, CON, MAC, GVL), NZ -> List(GRE, ASP, UNK, GRS, CON, Grass, Bitumen, paved, bitumen/gravel, COM, GVL, BIT, concrete, Sealed), GP -> List(ASP), NA -> List(ASP, GVL, UNK, null), JM -> List(ASP), GU -> List(ASP, PEM), SB -> List(Gravel dirt, grass coral, Coral grass, Soil and Grass, Limestone/Grass, Graded earth, Sand/grass, Sand grass, Coral, UNK, ASP, Grass, Coral sand, Crushed coral), CM -> List(ASP, UNK), US -> List(GRAVEL, GRVL-F, GRVL-G, TURF-G, GRAVEL-G, TURF-F, WATER, GRVL-DIRT-P, GRAVEL-P, WATER-E, GRVL-TURF-P, TURF-GRVL-P, TURF-P, ASPH-F, ASPH-P, ASPH-G, TURF, CONC, ASPH, ASPH-TURF, GRVL, ASPH-CONC, GRASS / SOD, ASP, DIRT, CONC-G, TURF-DIRT-G, MATS, ASPH-TRTD-G, CALICHE, ASPH-TURF-G, TURF-DIRT, DIRT-P, TURF-GRVL, DIRT-GRVL, ASPH-CONC-G, ROOF-TOP, CONC-GRVL-G, ASPH-GRVL, ASPH/ CONC, ALUM, WOOD, GRVL-DIRT-F, GRS, ASPH/GRVL-F, DIRT-TURF, PIERCED STEEL PLANKING / LANDING MATS / MEMBRANES, Water, GRE, GRVL-TURF, Grass, GVL, ASPH-TRTD-P, Asphalt, TURF-DIRT-F, TURF-GRVL-F, COP, ASPH-TURF-F, ASPH-E, TURF-E, GRAVEL / CINDERS / CRUSHED ROCK / CORAL/SHELLS / SLAG, GRVL-DIRT, DIRT-G, null, BIT, LAT, MATS-G, concrete, Gravel, asphalt, Turf, ASPH/CONC, PEM, grass, GRVL-P, GRVL-TURF-G, TURF-GRVL-G, DIRT-F, TRTD-DIRT, turf, CON, DECK, GRAVEL-F, Natural Soil, TRTD-DIRT-P, TURF-TRTD-G, NATURAL SOIL, CONC-TURF-G, None, CONC-TURF, WATER-G, SAND-F, ASPH-TRTD-F, GRAVEL, GRASS / SOD, COR, CORAL, GRVL-DIRT-G, GRVL-E, CONC-F, GRVL-DIRT-E, ASPH-GRVL-F, COM, DIRT-E, TRTD, TRTD-DIRT-F, Conc, BRICK, NEOPRENE, GRASS / SOD, GRAVEL, ASPH-TURF-P, ASPH-GRVL-G, CLA, PSP, DIRT-TRTD, GRVL-TRTD, GRASS / SOD, NATURAL SOIL, GRAVEL, TRTD, DIRT-GRVL-G, TURF-DIRT-P, DIRT-GRVL-F, METAL, GRASS, ASP/CONC, NATURAL SOIL, GRASS / SOD, ASPH-DIRT-P, UNK, PER, U, CONC-TRTD-G, CONC-E, ASPH-CONC-F, CONC-P, ASPH-GRVL-P, UNKNOWN, CONC-TRTD, ALUMINUM, CONC-GRVL, ASPH-TRTD, CONC-TURF-F, STEEL-CONC, DIRT-SAND, OILED DIRT, ASPH-DIRT, STEEL, ASPH-L, PFC, GRVL-TURF-F, DIRT-GRVL-P, ICE, GRVL-TRTD-F, ASPH-CONC-P, TREATED-E, GRVL-TRTD-P, TURF-SAND-F, NSTD, ROOFTOP, TREATED-G, DIRT-TURF-F, TREATED, ASPH-TURF-E, OIL&CHIP-T-G, TREATED-F, ALUM-DECK, ASPH-DIRT-G, GRASS-F, C, GRAVEL-E, SOD, SAND, DIRT-TURF-G), MV -> List(BIT, ASP, CON), SI -> List(grass, GRS, concrete, ASP, asphalt), CW -> List(ASP), BH -> List(ASP), VG -> List(ASP, UNK), HK -> List(ASP, Asphalt), SD -> List(gravel, Asphalt, ASP, UNK, GVL, graded earth), AD -> List(), RO -> List(grass, Grass, UNK, CON, ASP), LU -> List(grass, PEM), VC -> List(ASP), FO -> List(ASP), GL -> List(ICE, UNK, ASP, ASPH, GVL, CON), BW -> List(ASP, CON, GVL), CF -> List(ASP, UNK), CI -> List(ASP, CON, UNK), KY -> List(ASP), KG -> List(CON, concrete, ASP, ASB, asphalt), LY -> List(null, ASP, GVL, COP, SAN, CON), MM -> List(ASP, CON, BIT), MZ -> List(ASP, GVL, SAN, BIT), SZ -> List(ASP), IE -> List(ASPH, ASP, Grass, UNK, PEM, Bituminous, null, GRASS/HARDCORE, CON, BIT, GRASS), IR -> List(ASP, PEM, CON, GRE), EH -> List(ASP), IQ -> List(ASP, CON, PEM, Asphaltic Concrete, U, UNK, GRE), BB -> List(ASP), FK -> List(ASP), NP -> List(ASP, UNK, asphalt), BE -> List(UNK, CON, Grass, GRS, null, ASP, grass, PEM), AU -> List(X, PEM, N, L, B, S, G, ASP, C, UNK, GVL, Grass, CLA, GRS, UG, SAN, PER, NW, H, Gravel, Asphalt, Earth/sand, dirt, grass, gravel, COP, null, GRE, SU, XUG, CON, XSA, BIT, COM, Grass/Gravel, Grass and granite sand, GG, XS, NSGG, XU, NRC, paved, unsealed, sealed, TURF), TZ -> List(ASP, Hard, GRE, UNK, BIT), UY -> List(UNK, GRS, ASP, CON, PEM), SA -> List(ASP, BIT, CON, SAN, UNK), ZW -> List(ASP, MAC, BIT, GRS, GRE), MD -> List(CON, grass), PG -> List(Grassed brown silt clay, Grassed red clay, Grassed black silt sand, null, Grassed brown loam, Grassed yellow silt clay, Grassed brown gravel, Grassed brown clay, Grassed black silt, Red gravel, Grassed black silt clay, Grassed grey gravel, Grassed sand clay, Red clay, Brown gravel, Grassed black clay, Grassed red silt clay, Grassed white gravel, Grassed yellow gravel, GRS, Brown clay gravel, Grass/gravel, Grassed clay, Grassed sandy loam, Grassed silt clay, Brown clay, Grassed brown silt loam, Red clay gravel, Black clay, Grassed black soil, Black silt, Clay, Grassed brown sandy clay, Grassed white lime stone, Grassed grey clay, Grey gravel, Grassed blackclay, Grey clay, Gravel, Brown Silt clay, Grassed grey sand, Grassed red clay gravel, Grassed red silt, White gravel, Grassed gravel, Yellow gravel, Grassed yellow clay, Brown silt clay, Grassed grey silt clay, Grassed Brown Clay, ASP, GRE, GVL, UNK, Grey silt clay, Grassed Sand, Grassed grey silt sand, Grassed Red Clay, Grassed river gravel, Grass red silty clay, Grassed black clay silt, Grassed brown silty clay, Grassed clay silt clay, Grassed black clay sand, Grassed red silty clay, Grassed limestone gravel, Grassed red silt sand, Grassed brown clay gravel, Red silt clay, Grassed black sand, Hard loam, Sandy gravel with clay, Grassed white coronas), AF -> List(ASP, CON, CONCRETE AND ASP, GVL, GRV), MU -> List(ASP), SL -> List(ASP), HU -> List(UNK, concrete, ASP, GRS, CON, GRE, Concrete, grass, Grass), GT -> List(CON, ASP, Dirt/Gravel), BO -> List(ASP, CON, UNK, GRE), TM -> List(ASP, CON), NE -> List(ASP, LAT, UNK), CL -> List(GRV, GRE, ASP, GRASS, UNK, GRV/ASP, PAD, CON, GRV/GRASS, GRASS/PAD, PAD/GRASS, GRAIN, PAD/CON, GRV/PAD, BIT, SAND/GRASS, CLAY, GRV/MAICILLO, GVL, CON/PAD), FI -> List(ASP, GRASS, GRVL, ASPH/GRVL, Oilgravel/sand, GVL, COP, ASPH, Oilgravel, Sand, Oligravel/GRVL, COM, GRVL/GRASS, Asphalt, sand/grass, grass, ASPH/GRASS), MN -> List(UNK, GRE, ASP, CON, BIT), NO -> List(Ice, Grass, Gravel/Grass, ASP, Gravel, Ice - frozen lake, Asphalt, UNK, CON, Concrete), GG -> List(ASP, GRE), EE -> List(ASP, GRS, CON), KM -> List(ASP, UNK), LT -> List(paved, GRASS OR EARTH NOT GRADED OR ROLLED, ASP, UNK, CON, concrete, Asphalt, Grass, GRASS), KS -> List(ASP), ER -> List(ASP, CON), SH -> List(ASP), SY -> List(ASP, SAN, UNK, GRE, PEM, CON), LC -> List(ASP), CC -> List(ASP), PL -> List(Brick, grass, null, UNK, CON, Grass, GRS, ASP, asphalt, PEM), CH -> List(GRASS, CONC, ASP, asphalt, GRS, UNK, ASPH, CON, concrete, CONCRETE, CAUTION: ATC do NOT apply wake turbulence separation ! GRS.), ST -> List(ASP), NG -> List(ASP, CON, LAT), TF -> List(dirt), KI -> List(ASP, COR, BIT), LV -> List(CON, PEM, ASP), UG -> List(ASP, GRE, UNK), CY -> List(ASP, concrete), MF -> List(ASP), PM -> List(ASP), MW -> List(ASP, BIT, GRS), CG -> List(ASP, UNK), IS -> List(UNK, BIT, ASPH-G, ASP), BI -> List(UNK, ASP), SE -> List(Grass, Asphalt, Gravel, Pavement, ASP, UNK, GRS, PEM, CON, GRE, grass), AE -> List(ASP, U, Asphalt, MAC, UNK), KZ -> List(ASP, CON, GRE, GRS, asphalt, PEM), LB -> List(CON, ASP), AR -> List(ASP, GRE, CON, UNK, MET, STONE, Concrete), BF -> List(ASP, LAT), DJ -> List(PEM), HR -> List(CON, ASP, UNK, PEM, null, grass, GRE), BS -> List(ASP, ASPH, BIT, null), RS -> List(grass, GRS, ASP, Grass, Paved), BA -> List(Grass, ASP, GRASS, null), WS -> List(ASP), GB -> List(Asphalt, null, Grass, grass, Tarmac, GRS, ASP, Concrete, CON, PEM, UNK, asphalt, Concrete/Grass, Grass/Asphalt Insert 1968X59 Feet, SAND, U, TURF, GRE, GRASS, Grass/Asphalt, Concrete/Asphalt, Grass/Helipads Concrete, Graded Hardcore, Grass/Graded Hardcore, Gravel), FR -> List(UNK, GRS, ASP, CON, Grass, Asphalt, Paved, PEM, UNPAVED, CONCRETE, paved, Unpaved, UnPaved, Grass - Herbe, Grass - Herbe -> Avion - ULM, grass - herbe  (avion), grass - herbe  (planeur), Turf, Unknown, GRASS, MAC, null), GM -> List(ASP), LS -> List(GRE, ASP), UZ -> List(ASP, CON), PF -> List(ASP, COR, BIT, UNK), AG -> List(ASP), GW -> List(ASP, BIT), FJ -> List(UNK, ASP), CO -> List(ASP, GRE, CON, UNK), ZM -> List(ASP, null, CON, BRI, GRS, BIT, Gravel, UNK), AQ -> List(ice, GRE, SNO, ICE), GF -> List(CON, ASP), SO -> List(UNK, ASP, sand), MT -> List(ASP), NU -> List(ASP), BN -> List(ASP), RW -> List(ASP, grass, 'asphalt'), PT -> List(grav, null, GRAV, ASP, CON, UNK, asphalt, asph, GVL), PW -> List(ASP, GRAVEL-F, GRVL-F), KH -> List(GVL, ASP, CON, UNK, GRS), SX -> List(ASPH-CONC, ASP), TJ -> List(ASP, CON), KR -> List(CON, ASP, PEM, UNK, concrete, GRE, GVL, BIT, Graded earth, GRS), SS -> List(UNK, ASP, Asphalt), PY -> List(CON, ASP, UNK), AM -> List(ASP, CON, null, paved), MC -> List(), CX -> List(ASP), TT -> List(ASP), UA -> List(BRI, PEM, GRS, ASP, CON, UNK, GRE), LI -> List(), BR -> List(ASP, CON, GRE, GRS, Gravel, TER, MET/CON, SAND, GRE/GRS, CONC, ASP/GRE, GRVL, ASPH, MTAL, conc, SAI, GRASS, PIÇ, MET, ARG, ASP/GRS, GRAVEL, GRVL/PIÇ, WOOD, PIC, GVL, CONC/MTAL, CON/MET, CON/GRS, Grass, GRV, CON/ASP, UNK, Asphalt), PA -> List(Asphalt, ASP, CON), MQ -> List(ASP), TG -> List(ASP, CON), FM -> List(ASP), NR -> List(ASP), GN -> List(UNK, ASP), YT -> List(ASP), CD -> List(ASP, Dirt, GVL, LAT, PEM, CON), GA -> List(UNK, ASP, LAT), MG -> List(ASP, BIT, COP, null, PEM), AI -> List(ASP), YE -> List(UNK, ASP), HN -> List(ASP, GRASS, GRVL, CON, GRVL-GRASS), IT -> List(ASP, GRS, UNK, grass, BIT, Grass, BITUMINOUS, GRE, ground, asphalt, null, dirt, concrete), RE -> List(ASP), DO -> List(ASP, UNK, asphalt), IO -> List(CON), CZ -> List(UNK, ASP, CON, grass, Grass, GRS, null, Asphalt), GH -> List(ASP), GR -> List(ASP, UNK, PEM), AS -> List(ASP, CON, CONC-G), ZA -> List(Tar, Grass, Gravel, Grass/Concrete, asphalt, Soil, rough gravel, gravel, tar old, ASP, grass, use at own risk, animals may errode surface, dirt, use at own risk, animals may errode surface, tar, COM, GRE, GVL, LAT, Tar - lights 5 clicks on 124.8, GRS, UNK, Asphalt, hard, CON, paving), GY -> List(ASP, concrete), BY -> List(ASP, CON), LK -> List(Asphalt, ASP), BT -> List(ASP, Asphalt, BIT), OM -> List(ASP, GRE, GVL), CK -> List(CON, UNK, ASP, COR), KE -> List(ASP, BIT, UNK, GRE), MX -> List(ASP, Asphalt, asphalt, paved, packed dirt, Soil, CON, UNK, GRE, ASPHALTH, concrete), SK -> List(CON, UNK, GRS, ASP, grass), MK -> List(ASP, GRE, CON, GRS), DZ -> List(null, ASP, UNK, CON, PEM, BIT, SAN, GRE), QA -> List(Asphalt, ASP), CU -> List(ASP, CON), TL -> List(Grass, null, ASP), DK -> List(ASP, PEM, GRE, GRS, UNK, Grass, grass), BJ -> List(UNK, ASP), VI -> List(WATER, ASP), NL -> List(grass, ASP, asphalt, ASPH, grass, first 500x6 meter on 25 is paved, PEM, CON), LA -> List(ASP, CON, COP), CA -> List(gravel, TURF, TREATED GRAVEL, CRUSHED ROCK, ASP, GRAVEL, GRVL, GRAVEL/CLAY, GRS, GVL, Asphalt/Turf, asphalt, UNK, PEM, CON, ASPHALT, COM, ASPH, GRAVEL/SAND, Asphalt, SAN, SAND/GRAVEL, TURF/SNOW, Gravel, GRASS, GRASS/SNOW, GRAVEL/TURF, Turf/Snow, SOFT SAND, Turf, EARTH/SNOW, TURF/GRAVEL, GRASS/GRAVEL, COP, GRAVEL/GRASS, turf, SAND, TRTD GRVL, GRASS&amp;GRAVEL, Sand, SNOW, TURF/GRVL, CLAY/GRVL, GRVL/TURF, TURF/ASP, ASP/TURF, GRVL/ASP, water, SAND/GRVL, null, GRVL/CLAY, OLD ASP, CLAY/TURF, Grass, TURF/ASPHALT, CLAY, TREATED SAND, TURF/CLAY, CLAY/GRAVEL, TURF/TREATED G, TURF/EARTH, OILED GRAVEL, GRAVEL/SAND/CL, CLAY/SAND, EARTH, TURF/GRAVEL/SN, CONC/GRVL, GRE, TURF/EARTH/GRA, TURF/GRAVEL/CL, PACKED GRAVEL, GRAVEL/CLAY/SA, ASPHALT/TURF, CLAY/GRAVEL/TU, SAND/CLAY/GRAV, EARTH/TURF, TURF/SOIL, OILED GRAVEL/T, TURF/OIL PACKE, Turf / Snow, OILED, Gravel/Snow, Turf / Gravel, LOOSE GRAVEL, Gravel/Asphalt mix, TURF/GRAVEL/AS, SAND/GRAVEL/AS, asp, TURF/CHIPSEAL, paved, DIRT, grass, Ice, dirt, No winter maint., asphalt/gravel, Stone Dust, Turf, soft during spring thaw), BM -> List(ASP), JP -> List(concrete, ASP, UNK, U, CON, PSP, COR, PEM, GRE, GRS), AW -> List(ASP), TO -> List(BIT, UNK, GRS, ASP), CN -> List(CON, ASP, UNK, Concrete, Asphalt, concrete, paved, null), VU -> List(ASP, GRS, Grass, Grass on coral, Coral, Volcanic ash/soil, Asphalt, Volcanic ash impregnated with bitumen), AL -> List(GRE, ASP, CON, paved, GRS), ET -> List(ASP, PEM, CON), IM -> List(ASP), SN -> List(ASP, MAC, GRE, SAN), PE -> List(ASP, CON, UNK), BQ -> List(ASP), CR -> List(ASP, UNK, CON), VA -> List(), NC -> List(ASP, Paved/Gravel, MAC, UNK, Asphalt, Paved/Compacted schist), MP -> List(TURF-GRVL, PEM, ASP), GE -> List(CON, ASP), TD -> List(ASP, UNK), SC -> List(CON, ASP), PS -> List(ASP), EC -> List(ASP, Paviment hard, UNK), TV -> List(UNK), LR -> List(ASP), MS -> List(ASP), DE -> List(CON, ASP, PEM, GRS, Asphalt, grass, UNK, concrete, ASPHALT, TURF, Concrete, ASPH, Paved, U, Grass, CONCRETE + GRASS. MTOM 2t, GRASS, gras, asphalt), SR -> List(ASPHALT))""")

  val (lowAirportNumberCountry, highAirportNumberCountry) = getNumberOfAirportPerCountry()

  lowAirportNumberCountry should have size 10
  lowAirportNumberCountry.toString should be ("List((TK,0), (GS,0), (PN,0), (JE,1), (GI,1), (NF,1), (MO,1), (ZZ,1), (BL,1), (CW,1))")

  highAirportNumberCountry should have size 10
  highAirportNumberCountry.toString should be ("List((VE,592), (CO,700), (DE,703), (AR,713), (FR,789), (RU,920), (AU,1908), (CA,2454), (BR,3839), (US,21501))")


  import scala.concurrent.duration._
  val postRequest = HttpRequest(
    HttpMethods.POST,
    uri = "/find",
    entity = HttpEntity(MediaTypes.`application/json`, s"""{ "pays" : "France" }"""))

  postRequest ~> routes ~> check {
    status.isSuccess() shouldEqual true
    Await.result(responseEntity.dataBytes.map(_.decodeString("UTF8")).toMat(Sink.fold("")((acc, elem) => acc + elem))(Keep.right).run, 2 seconds).length should be > 3
  }


  val badPostRequest = HttpRequest(
    HttpMethods.POST,
    uri = "/find",
    entity = HttpEntity(MediaTypes.`application/json`, s"""{ "anyJsonKey" : "anyValue" }"""))

  badPostRequest ~> routes ~> check {
    status.isSuccess() shouldEqual false
  }
}
