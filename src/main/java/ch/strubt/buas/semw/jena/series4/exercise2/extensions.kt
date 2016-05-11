package ch.strubt.buas.semw.jena.series4.exercise2

inline fun <T : AutoCloseable, R> T.use(block: (T) -> R): R {

	var closed = false
	try {
		return block(this)

	} catch (e: Exception) {
		closed = true

		try {
			close()

		} catch (closeException: Exception) {
		}
		throw e

	} finally {
		if (!closed)
			close()
	}
}
