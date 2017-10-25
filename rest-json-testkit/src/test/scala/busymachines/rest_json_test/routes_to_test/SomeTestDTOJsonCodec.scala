package busymachines.rest_json_test.routes_to_test

/**
  *
  * Defining JSON encoders like this greatly increases compilation speed, and you only
  * have to derive the top-most types anyway. Nested types of [[SomeTestDTOPost]], etc.
  * are still derived automatically.
  *
  * @author Lorand Szakacs, lsz@lorandszakacs.com, lorand.szakacs@busymachines.com
  * @since 19 Oct 2017
  *
  */
private[rest_json_test] object SomeTestDTOJsonCodec extends SomeTestDTOJsonCodec

private[rest_json_test] trait SomeTestDTOJsonCodec {

  import busymachines.json._
  import busymachines.json.derive.defaultDerivationConfiguration

  implicit val someTestDTOGetCodec: Codec[SomeTestDTOGet] = derive.codec[SomeTestDTOGet]
  implicit val someTestDTOPostCodec: Codec[SomeTestDTOPost] = derive.codec[SomeTestDTOPost]
  implicit val someTestDTOPutCodec: Codec[SomeTestDTOPut] = derive.codec[SomeTestDTOPut]
  implicit val someTestDTOPatchCodec: Codec[SomeTestDTOPatch] = derive.codec[SomeTestDTOPatch]

}
