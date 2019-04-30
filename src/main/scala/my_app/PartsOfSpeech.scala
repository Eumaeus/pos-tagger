package posTagger 

import com.thoughtworks.binding.{Binding, dom}
import com.thoughtworks.binding.Binding.{BindingSeq, Var, Vars}
import scala.scalajs.js
import scala.scalajs.js._
import org.scalajs.dom._
import org.scalajs.dom.ext._
import scala.scalajs.js.Dynamic.{ global => g }
import org.scalajs.dom.raw._
import org.scalajs.dom.document
import org.scalajs.dom.raw.Event
import org.scalajs.dom.ext.Ajax
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citeobj._
import scala.scalajs.js.annotation.JSExport
import js.annotation._


@JSExportTopLevel("posTagger.PosTags")
object PosTags {

case class PosElement(short:String, long:String, tag:String)

def makeEmptyPosElement():PosElement = {
	PosElement("none", "None", "-")
}

case class PartOfSpeech(
	pos:Option[PosElement],
	person:Option[PosElement],
	number:Option[PosElement],
	tense:Option[PosElement],
	mood:Option[PosElement],
	voice:Option[PosElement],
	gender:Option[PosElement],
	grammaticalcase:Option[PosElement],
	degree:Option[PosElement]
) {

	override def toString:String = {
		s"${this.toLabel}: ( ${this.toPosTag} )"
	}

	def toPosTag:String = {
		val posStr:String = {
			pos match {
				case Some(s) => s.tag
				case None => "-"
			}
		}
		val personStr:String = {
			person match {
				case Some(s) => s.tag
				case None => "-"
			}
		}
		val numberStr:String = {
			number match {
				case Some(s) => s.tag
				case None => "-"
			}
		}
		val tenseStr:String = {
			tense match {
				case Some(s) => s.tag
				case None => "-"
			}
		}
		val voiceStr:String = {
			voice match {
				case Some(s) => s.tag
				case None => "-"
			}
		}
		val moodStr:String = {
			mood match {
				case Some(s) => s.tag
				case None => "-"
			}
		}
		val genderStr:String = {
			gender match {
				case Some(s) => s.tag
				case None => "-"
			}
		}
		val grammaticalcaseStr:String = {
			grammaticalcase match {
				case Some(s) => s.tag
				case None => "-"
			}
		}
		val degreeStr:String = {
			degree match {
				case Some(s) => s.tag
				case None => "-"
			}
		}
		s"${posStr}${personStr}${numberStr}${tenseStr}${moodStr}${voiceStr}${genderStr}${grammaticalcaseStr}${degreeStr}"
	}

	def toLabel:String = {
	val posStr:String = {
			pos match {
				case Some(s) => s.long + ", "
				case None => ""
			}
		}
		val personStr:String = {
			person match {
				case Some(s) => s.long + ", "
				case None => ""
			}
		}
		val numberStr:String = {
			number match {
				case Some(s) => s.long + ", "
				case None => ""
			}
		}
		val tenseStr:String = {
			tense match {
				case Some(s) => s.long + ", "
				case None => ""
			}
		}
		val voiceStr:String = {
			voice match {
				case Some(s) => s.long + ", "
				case None => ""
			}
		}
		val moodStr:String = {
			mood match {
				case Some(s) => s.long + ", "
				case None => ""
			}
		}
		val genderStr:String = {
			gender match {
				case Some(s) => s.long + ", "
				case None => ""
			}
		}
		val grammaticalcaseStr:String = {
			grammaticalcase match {
				case Some(s) => s.long + ", "
				case None => ""
			}
		}
		val degreeStr:String = {
			degree match {
				case Some(s) => s.long + ", "
				case None => ""
			}
		}
		val commaString:String = s"Part of Speech: ${posStr}${personStr}${numberStr}${tenseStr}${moodStr}${voiceStr}${genderStr}${grammaticalcaseStr}${degreeStr}"

		commaString.takeRight(2) match {
			case ", " => commaString.dropRight(2)
			case _ => commaString
		}
	}
}



val posSchema:Vector[Map[String,String]] = Vector(
	Map("short" -> "pos", "long" -> "Part of Speech"),
	Map("short" -> "pers", "long" -> "Person"),
	Map("short" -> "num", "long" -> "Number"),
	Map("short" -> "tense", "long" -> "Tense"),
	Map("short" -> "voice", "long" -> "Voice"),
	Map("short" -> "mood", "long" -> "Mood"),
	Map("short" -> "gend", "long" -> "Gender"),
	Map("short" -> "case", "long" -> "Case"),
	Map("short" -> "degree", "long" -> "Degree")
)

val partsOfSpeech:Vector[Map[String,String]] = Vector(
	Map("short" -> "none", "long" -> "None", "tag" -> "-"),
	Map("short" -> "art", "long" -> "Article", "tag" -> "l"),
	Map("short" -> "noun", "long" -> "Noun", "tag" -> "n"),
	Map("short" -> "adj", "long" -> "Adjective", "tag" -> "a"),
	Map("short" -> "pron", "long" -> "Pronoun", "tag" -> "p"),
	Map("short" -> "verb", "long" -> "Verb", "tag" -> "v"),
	Map("short" -> "adv", "long" -> "Adverb", "tag" -> "d"),
	Map("short" -> "prep", "long" -> "Preposition", "tag" -> "r"),
	Map("short" -> "conj", "long" -> "Conjunction", "tag" -> "c"),
	Map("short" -> "int", "long" -> "Interjection", "tag" -> "i"),
	Map("short" -> "punct", "long" -> "Punctuation", "tag" -> "u"),
	Map("short" -> "irreg", "long" -> "Irregular", "tag" -> "x")
)

val persons:Vector[Map[String,String]] = Vector(
	Map("short" -> "none", "long" -> "None", "tag" -> "-"),
	Map("short" -> "1st", "long" -> "First Person", "tag" -> "1"),
	Map("short" -> "2nd", "long" -> "Second Person", "tag" -> "2"),
	Map("short" -> "3rd", "long" -> "Third Person", "tag" -> "3")
)

val numbers:Vector[Map[String,String]] = Vector(
	Map("short" -> "none", "long" -> "None", "tag" -> "-"),
	Map("short" -> "sg", "long" -> "Singular", "tag" -> "s"),
	Map("short" -> "pl", "long" -> "Plural", "tag" -> "p"),
	Map("short" -> "dl", "long" -> "Dual", "tag" -> "d")
)

val tenses:Vector[Map[String,String]] = Vector(
	Map("short" -> "none", "long" -> "None", "tag" -> "-"),
	Map("short" -> "pr", "long" -> "Present", "tag" -> "p"),
	Map("short" -> "imp", "long" -> "Imperfect", "tag" -> "i"),
	Map("short" -> "perf", "long" -> "Perfect", "tag" -> "r"),
	Map("short" -> "pqpf", "long" -> "Pluperfect", "tag" -> "l"),
	Map("short" -> "fex", "long" -> "Future Perfect", "tag" -> "t"),
	Map("short" -> "fut", "long" -> "Future", "tag" -> "f"),
	Map("short" -> "aor", "long" -> "Aorist", "tag" -> "a")
)

val moods:Vector[Map[String,String]] = Vector(
	Map("short" -> "none", "long" -> "None", "tag" -> "-"),
	Map("short" -> "ind", "long" -> "Indicative", "tag" -> "i"),
	Map("short" -> "sub", "long" -> "Subjunctive", "tag" -> "s"),
	Map("short" -> "inf", "long" -> "Infinitive", "tag" -> "n"),
	Map("short" -> "imp", "long" -> "Imperative", "tag" -> "m"),
	Map("short" -> "part", "long" -> "Participle", "tag" -> "p"),
	Map("short" -> "opt", "long" -> "Optative", "tag" -> "o")
)

val voices:Vector[Map[String,String]] = Vector(
	Map("short" -> "none", "long" -> "None", "tag" -> "-"),
	Map("short" -> "act", "long" -> "Active", "tag" -> "a"),
	Map("short" -> "mid", "long" -> "Middle", "tag" -> "m"),
	Map("short" -> "Pass", "long" -> "Passive", "tag" -> "p"),
	Map("short" -> "mp", "long" -> "Medio-Passive", "tag" -> "e"),
	Map("short" -> "dep", "long" -> "Depondent", "tag" -> "d")
)

val genders:Vector[Map[String,String]] = Vector(
	Map("short" -> "none", "long" -> "None", "tag" -> "-"),
	Map("short" -> "masc", "long" -> "Masculine", "tag" -> "m"),
	Map("short" -> "fem", "long" -> "Feminine", "tag" -> "f"),
	Map("short" -> "neut", "long" -> "Neuter", "tag" -> "n")
)

val cases:Vector[Map[String,String]] = Vector(
	Map("short" -> "none", "long" -> "None", "tag" -> "-"),
	Map("short" -> "nom", "long" -> "Nominative", "tag" -> "n"),
	Map("short" -> "gen", "long" -> "Genitive", "tag" -> "g"),
	Map("short" -> "dat", "long" -> "Dative", "tag" -> "d"),
	Map("short" -> "acc", "long" -> "Accusative", "tag" -> "a"),
	Map("short" -> "voc", "long" -> "Vocative", "tag" -> "v"),
	Map("short" -> "loc", "long" -> "Locative", "tag" -> "l")
)

val degrees:Vector[Map[String,String]] = Vector(
	Map("short" -> "none", "long" -> "None", "tag" -> "-"),
	Map("short" -> "pos", "long" -> "Positive", "tag" -> "p"),
	Map("short" -> "comp", "long" -> "Comparative", "tag" -> "c"),
	Map("short" -> "sup", "long" -> "Superlative", "tag" -> "s")
)

}