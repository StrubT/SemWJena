package ch.strubt.buas.semw.jena.series4.exercise2

import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.query.ResultSetFormatter
import org.apache.jena.rdf.model.ModelFactory

fun main(vararg args: String) {

	cdmusic()
	family()
	endpoint()
}

object Resources {

	val cdmusicData = Resources::class.java.getResourceAsStream("cdmusic.ttl").reader().readText()

	val cdmusicQueries = listOf(Resources::class.java.getResourceAsStream("cdmusic-1.rq").reader().readText(),
															Resources::class.java.getResourceAsStream("cdmusic-2.rq").reader().readText(),
															Resources::class.java.getResourceAsStream("cdmusic-3.rq").reader().readText(),
															Resources::class.java.getResourceAsStream("cdmusic-4.rq").reader().readText(),
															Resources::class.java.getResourceAsStream("cdmusic-5.rq").reader().readText())

	val familyData = Resources::class.java.getResourceAsStream("family.rdf").reader().readText()

	val familyQueryConstruct = Resources::class.java.getResourceAsStream("family-construct.rq").reader().readText()
	val familyQueryAsk = Resources::class.java.getResourceAsStream("family-ask.rq").reader().readText()

	val dbpediaService = "http://dbpedia.org/sparql"

	val dbpediaQueries = listOf(Resources::class.java.getResourceAsStream("dbpedia-0.rq").reader().readText(),
															Resources::class.java.getResourceAsStream("dbpedia-1.rq").reader().readText(),
															Resources::class.java.getResourceAsStream("dbpedia-2.rq").reader().readText())
}

fun cdmusic() {

	val model = ModelFactory.createDefaultModel()
	model.read(Resources.cdmusicData.reader(), null, "TURTLE")

	for (queryString in Resources.cdmusicQueries) {
		val query = QueryFactory.create(queryString)

		QueryExecutionFactory.create(query, model).use { execution ->
			val resultSet = execution.execSelect()
			ResultSetFormatter.out(System.out, resultSet, query)
		}
	}
}

fun family() {

	val model = ModelFactory.createDefaultModel()
	model.read(Resources.familyData.reader(), null)

	val queryConstruct = QueryFactory.create(Resources.familyQueryConstruct)
	QueryExecutionFactory.create(queryConstruct, model).use { execution ->
		val resultModel = execution.execConstruct()
		resultModel.write(System.out, "TURTLE")
	}

	val queryAsk = QueryFactory.create(Resources.familyQueryAsk)
	QueryExecutionFactory.create(queryAsk, model).use { execution ->
		val resultAnswer = execution.execAsk()
		println(resultAnswer)
	}
}

fun endpoint() {

	for (queryString in Resources.dbpediaQueries) {
		val query = QueryFactory.create(queryString)

		QueryExecutionFactory.sparqlService(Resources.dbpediaService, query).use { execution ->
			val resultSet = execution.execSelect()
			ResultSetFormatter.out(System.out, resultSet, query)
		}
	}
}
