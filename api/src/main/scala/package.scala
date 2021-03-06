package reactivemongo.api

/**
 * {{{
 * import reactivemongo.api.bson._
 *
 * // { "name": "Johny", "surname": "Doe", "age": 28, "months": [1, 2, 3] }
 * document ++ ("name" -> "Johny") ++ ("surname" -> "Doe") ++
 * ("age" -> 28) ++ ("months" -> array(1, 2, 3))
 *
 * // { "_id": generatedId, "name": "Jane", "surname": "Doe", "age": 28,
 * //   "months": [1, 2, 3], "details": { "salary": 12345,
 * //   "inventory": ["foo", 7.8, 0, false] } }
 * document ++ ("_id" -> generateId, "name" -> "Jane", "surname" -> "Doe",
 *   "age" -> 28, "months" -> array(1, 2, 3),
 *   "details" -> document(
 *     "salary" -> 12345L, "inventory" -> array("foo", 7.8, 0L, false)))
 * }}}
 */
package object bson extends DefaultBSONHandlers with Aliases with Utils {
  // DSL helpers:

  /** Returns an empty document. */
  def document = BSONDocument.empty

  /** Returns a document with given elements. */
  def document(elements: ElementProducer*) = BSONDocument(elements: _*)

  /** Returns an empty array. */
  def array = BSONArray.empty

  /** Returns an array with given values. */
  def array(values: Producer[BSONValue]*) = BSONArray(values: _*)

  /** Returns a newly generated object ID. */
  def generateId = BSONObjectID.generate()

  def element(name: String, value: BSONValue) = BSONElement(name, value)

  /** Convenient type alias for document handlers */
  type BSONDocumentHandler[T] = BSONDocumentReader[T] with BSONDocumentWriter[T] with BSONHandler[T]

  /** Handler factory */
  object BSONDocumentHandler {
    def apply[T](
      read: BSONDocument => T,
      write: T => BSONDocument): BSONDocumentHandler[T] =
      new FunctionalDocumentHandler[T](read, write)

    def provided[T](implicit r: BSONDocumentReader[T], w: BSONDocumentWriter[T]): BSONDocumentHandler[T] = new DefaultDocumentHandler[T](r, w)
  }
}
