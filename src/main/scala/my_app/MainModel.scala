package posTagger 
import com.thoughtworks.binding.{Binding, dom}
import com.thoughtworks.binding.Binding.{BindingSeq, Var, Vars}
import com.thoughtworks.binding.Binding.{Var, Vars}
import com.thoughtworks.binding.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.Event
import org.scalajs.dom.ext.Ajax
import scala.concurrent
              .ExecutionContext
              .Implicits
              .global

import scala.scalajs.js
import scala.scalajs.js._
import edu.holycross.shot.cite._
import js.annotation._
import edu.holycross.shot.scm._
import scala.scalajs.js.Dynamic.{ global => g }



@JSExportTopLevel("posTagger.MainModel")
object MainModel {

	val userMessage = Var("Main loaded.")
	val userAlert = Var("default")
	val userMessageVisibility = Var("app_hidden")

	var msgTimer:scala.scalajs.js.timers.SetTimeoutHandle = null

	val currentLibraryMetadataString = Var("No library loaded.")

	val currentPos = Var(PosTags.PartOfSpeech(
		None, None, None, None, None, None, None, None, None
	))

	val blankPos = PosTags.PartOfSpeech(
		None, None, None, None, None, None, None, None, None
	)


	val boundPartsOfSpeech = Vars.empty[Map[String,String]]
	for (c <- PosTags.partsOfSpeech) { boundPartsOfSpeech.value += c}


	val boundPersons = Vars.empty[PosTags.PosElement]
	val boundNumbers = Vars.empty[PosTags.PosElement]
	val boundTenses  = Vars.empty[PosTags.PosElement]
	val boundMoods   = Vars.empty[PosTags.PosElement]
	val boundVoices  = Vars.empty[PosTags.PosElement]
	val boundCases   = Vars.empty[PosTags.PosElement]
	val boundGenders = Vars.empty[PosTags.PosElement]
	val boundDegrees = Vars.empty[PosTags.PosElement]


	def initBoundPersons():Unit = {
		boundPersons.value.clear 	
		boundPersons.value += PosTags.makeEmptyPosElement
	}
	def allBoundPersons(omit:Vector[String] = Vector()):Unit = {
		boundPersons.value.clear 	
		val tempVec:Vector[PosTags.PosElement] = {
			val tempMaps = PosTags.persons.filter(t => {
				!(omit.contains(t("short")))
			})
			tempMaps.map( t => { 
				PosTags.PosElement( t("short"), t("long"), t("tag") )
			})
		}
		for (p <- tempVec){ boundPersons.value += p }
	}

	def initBoundNumbers():Unit = {
		boundNumbers.value.clear 	
		boundNumbers.value += PosTags.makeEmptyPosElement
	}
	def allBoundNumbers(omit:Vector[String] = Vector()):Unit = {
		boundNumbers.value.clear 	
		val tempVec:Vector[PosTags.PosElement] = {
			val tempMaps = PosTags.numbers.filter(t => {
				!(omit.contains(t("short")))
			})
			tempMaps.map( t => { 
				PosTags.PosElement( t("short"), t("long"), t("tag") )
			})
		}
		for (n <- tempVec){ boundNumbers.value += n }
	}

	def initBoundTenses():Unit = {
		boundTenses.value.clear 	
		boundTenses.value += PosTags.makeEmptyPosElement
	}
	def allBoundTenses(omit:Vector[String] = Vector()):Unit = {
		boundTenses.value.clear
		val tempVec:Vector[PosTags.PosElement] = {
			val tempMaps = PosTags.tenses.filter(t => {
				!(omit.contains(t("short")))
			})
			tempMaps.map( t => { 
				PosTags.PosElement( t("short"), t("long"), t("tag") )
			})
		}	
		for (t <- tempVec){ boundTenses.value += t }
	}



	def initBoundMoods():Unit = {
		boundMoods.value.clear 	
		boundMoods.value += PosTags.makeEmptyPosElement
	}
	def allBoundMoods(omit:Vector[String] = Vector()):Unit = {
		boundMoods.value.clear 	
		val tempVec:Vector[PosTags.PosElement] = {
			val tempMaps = PosTags.moods.filter(t => {
				!(omit.contains(t("short")))
			})
			tempMaps.map( t => { 
				PosTags.PosElement( t("short"), t("long"), t("tag") )
			})
		}	
		for (t <- tempVec){ boundMoods.value += t }
	}

	def initBoundVoices():Unit = {
		boundVoices.value.clear 	
		boundVoices.value += PosTags.makeEmptyPosElement
	}
	def allBoundVoices():Unit = {
		boundVoices.value.clear 	
		for (n <- PosTags.voices){
			boundVoices.value += PosTags.PosElement(n("short"), n("long"), n("tag"))
		}
	}

	def initBoundCases():Unit = {
		boundCases.value.clear 	
		boundCases.value += PosTags.makeEmptyPosElement
	}
	def allBoundCases():Unit = {
		boundCases.value.clear 	
		for (n <- PosTags.cases){
			boundCases.value += PosTags.PosElement(n("short"), n("long"), n("tag"))
		}
	}

	def initBoundGenders():Unit = {
		boundGenders.value.clear 	
		boundGenders.value += PosTags.makeEmptyPosElement
	}
	def allBoundGenders():Unit = {
		boundGenders.value.clear 	
		for (n <- PosTags.genders){
			boundGenders.value += PosTags.PosElement(n("short"), n("long"), n("tag"))
		}
	}

	def initBoundDegrees():Unit = {
		boundDegrees.value.clear 	
		boundDegrees.value += PosTags.makeEmptyPosElement
	}
	def allBoundDegrees():Unit = {
		boundDegrees.value.clear 	
		for (n <- PosTags.degrees){
			boundDegrees.value += PosTags.PosElement(n("short"), n("long"), n("tag"))
		}
	}


	def updateCurrentPosWithPartOfSpeech(partOfSpeechTag:String):Unit = {
		currentPos.value = blankPos
		val newPosCandidate:Vector[Map[String,String]] = PosTags.partsOfSpeech.filter( _("tag") == partOfSpeechTag )
		if (newPosCandidate.size > 0){
			val newPosEl:PosTags.PosElement = PosTags.PosElement(
				newPosCandidate(0)("short"),
				newPosCandidate(0)("long"),
				newPosCandidate(0)("tag")
			)
			val newPos:PosTags.PartOfSpeech = {
				newPosEl.short match {
					case "none" => {
						PosTags.PartOfSpeech(
							None, None, None, None, None, None, None, None, None
					    )
					}
					case _ => {
						PosTags.PartOfSpeech(
							Some(newPosEl),
							currentPos.value.person,
							currentPos.value.number,
							currentPos.value.tense,
							currentPos.value.mood,
							currentPos.value.voice,
							currentPos.value.gender,
							currentPos.value.grammaticalcase,
							currentPos.value.degree
						)
					}
				}
			}
			currentPos.value = newPos

			// Now do the updates 
			currentPos.value.pos match {
				case Some(p) => {
					p.short match {	
						case "verb" => {
							initBoundCases()
							initBoundGenders()
							initBoundNumbers()
							initBoundPersons()
							initBoundTenses()
							allBoundMoods()
							initBoundVoices()
							initBoundDegrees()
						}
						case "art" => {
							allBoundCases()
							allBoundGenders()
							allBoundNumbers()
							initBoundPersons()
							initBoundMoods()
							initBoundTenses()
							initBoundVoices()
							initBoundDegrees()
						}
						case "noun" => {
							allBoundCases()
							allBoundGenders()
							allBoundNumbers()
							initBoundPersons()
							initBoundMoods()
							initBoundTenses()
							initBoundVoices()
							initBoundDegrees()
						}
						case "adj" => {
							allBoundCases()
							allBoundGenders()
							allBoundNumbers()
							initBoundPersons()
							initBoundMoods()
							initBoundTenses()
							initBoundVoices()
							allBoundDegrees()
						}
						case "adv" => {
							initBoundCases()
							initBoundGenders()
							initBoundNumbers()
							initBoundPersons()
							initBoundMoods()
							initBoundTenses()
							initBoundVoices()
							allBoundDegrees()
						}
						case "pron" => {
							allBoundCases()
							allBoundGenders()
							allBoundNumbers()
							initBoundPersons()
							initBoundMoods()
							initBoundTenses()
							initBoundVoices()
							initBoundDegrees()
						}
						case _ => {
							initBoundMoods()
							updateCurrentPosWithMood("-")
							initBoundTenses()
							updateCurrentPosWithTense("-")
							initBoundPersons()
							updateCurrentPosWithPerson("-")
							initBoundNumbers()
							updateCurrentPosWithNumber("-")
							initBoundVoices()
							updateCurrentPosWithVoice("-")
						}
					}
				}
				case None => {
					initBoundPersons()
					initBoundNumbers()
					initBoundTenses()
					initBoundMoods()
					initBoundVoices()
					initBoundCases()
					initBoundGenders()
					initBoundDegrees()
				}
			}
		}
	}

	def updateCurrentPosWithMood(moodTag:String):Unit = {
		val newMoodCandidate:Vector[Map[String,String]] = PosTags.moods.filter( _("tag") == moodTag )
		if (newMoodCandidate.size > 0){
			val newMoodEl:PosTags.PosElement = PosTags.PosElement(
				newMoodCandidate(0)("short"),
				newMoodCandidate(0)("long"),
				newMoodCandidate(0)("tag")
			)
			val newPos:PosTags.PartOfSpeech = {
				newMoodEl.short match {
					case "none" => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							None,
							None,
							currentPos.value.tense,
							None,
							currentPos.value.voice,
							currentPos.value.gender,
							currentPos.value.grammaticalcase,
							currentPos.value.degree
						)
					}	
					case _ => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							currentPos.value.person,
							currentPos.value.number,
							currentPos.value.tense,
							Some(newMoodEl),
							currentPos.value.voice,
							currentPos.value.gender,
							currentPos.value.grammaticalcase,
							currentPos.value.degree
						)
					}
				}
			}
			currentPos.value = newPos

			// Now do the updates 
			currentPos.value.mood match {
				case Some(m) => {
					m.short match {	
						case "part" => {
							initBoundPersons()
							allBoundNumbers()
							allBoundCases()
							allBoundGenders()
							allBoundVoices()
							allBoundTenses(Vector("imp","pqpf"))
							//allBoundVoices
						}
						case "inf" => {
							initBoundPersons()
							initBoundNumbers()
							initBoundCases()
							initBoundGenders()
							allBoundVoices()
							allBoundTenses(Vector("imp","pqpf"))

							//allBoundVoices
						}
						case _ => {
							initBoundCases()
							initBoundGenders()
							allBoundVoices()
							allBoundNumbers()
							//allBoundVoices
							m.short match {
								case "ind" => {
									allBoundTenses()
									allBoundPersons()	
								}
								case "sub" => {
									// tense on present, aorist, perfect only
									allBoundTenses(Vector("imp", "pqpf", "fex", "fut"))
									allBoundPersons()	
								}
								case "opt" => {
									// tense on pres, aorist, future only
									allBoundTenses(Vector("imp", "pqpf", "fex", "perf"))
									allBoundPersons()	
								}
								case "imp" => {
									allBoundPersons(Vector("1st"))	
									allBoundTenses(Vector("imp", "pqpf", "fex", "fut"))
								}
								case _ => // do nothing
							}
						}
					}
				}
				case None => {
					initBoundPersons()
					initBoundNumbers()
					initBoundCases()
					initBoundGenders()
					initBoundDegrees()
				}
			}
		}	
	}

	def updateCurrentPosWithVoice(voiceTag:String):Unit = {
		val newVoiceCandidate:Vector[Map[String,String]] = PosTags.voices.filter( _("tag") == voiceTag )
		if (newVoiceCandidate.size > 0){
			val newVoiceEl:PosTags.PosElement = PosTags.PosElement(
				newVoiceCandidate(0)("short"),
				newVoiceCandidate(0)("long"),
				newVoiceCandidate(0)("tag")
			)
			val newPos:PosTags.PartOfSpeech = {
				newVoiceEl.short match {
					case "none" => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							currentPos.value.person,
							currentPos.value.number,
							currentPos.value.tense,
							None,
							currentPos.value.mood,
							currentPos.value.gender,
							currentPos.value.grammaticalcase,
							currentPos.value.degree
						)
					}	
					case _ => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							currentPos.value.person,
							currentPos.value.number,
							currentPos.value.tense,
							Some(newVoiceEl),
							currentPos.value.mood,
							currentPos.value.gender,
							currentPos.value.grammaticalcase,
							currentPos.value.degree
						)
					}
				}
			}
			currentPos.value = newPos
		}
	}

	def updateCurrentPosWithPerson(personTag:String):Unit = {
		val newPersonCandidate:Vector[Map[String,String]] = PosTags.persons.filter( _("tag") == personTag )
		if (newPersonCandidate.size > 0){
			val newPersonEl:PosTags.PosElement = PosTags.PosElement(
				newPersonCandidate(0)("short"),
				newPersonCandidate(0)("long"),
				newPersonCandidate(0)("tag")
			)
			val newPos:PosTags.PartOfSpeech = {
				newPersonEl.short match {
					case "none" => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							None,
							currentPos.value.number,
							currentPos.value.tense,
							currentPos.value.voice,
							currentPos.value.mood,
							currentPos.value.gender,
							currentPos.value.grammaticalcase,
							currentPos.value.degree
						)
					}	
					case _ => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							Some(newPersonEl),
							currentPos.value.number,
							currentPos.value.tense,
							currentPos.value.voice,
							currentPos.value.mood,
							currentPos.value.gender,
							currentPos.value.grammaticalcase,
							currentPos.value.degree
						)
					}
				}
			}
			currentPos.value = newPos
		}
	}

	def updateCurrentPosWithNumber(numberTag:String):Unit = {
		val newNumberCandidate:Vector[Map[String,String]] = PosTags.numbers.filter( _("tag") == numberTag )
		if (newNumberCandidate.size > 0){
			val newNumberEl:PosTags.PosElement = PosTags.PosElement(
				newNumberCandidate(0)("short"),
				newNumberCandidate(0)("long"),
				newNumberCandidate(0)("tag")
			)
			val newPos:PosTags.PartOfSpeech = {
				newNumberEl.short match {
					case "none" => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							currentPos.value.person,
							None,
							currentPos.value.tense,
							currentPos.value.voice,
							currentPos.value.mood,
							currentPos.value.gender,
							currentPos.value.grammaticalcase,
							currentPos.value.degree
						)
					}	
					case _ => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							currentPos.value.person,
							Some(newNumberEl),
							currentPos.value.tense,
							currentPos.value.voice,
							currentPos.value.mood,
							currentPos.value.gender,
							currentPos.value.grammaticalcase,
							currentPos.value.degree
						)
					}
				}
			}
			currentPos.value = newPos
		}
	}

	def updateCurrentPosWithCase(caseTag:String):Unit = {
    	val newCaseCandidate:Vector[Map[String,String]] = PosTags.cases.filter( _("tag") == caseTag )
		if (newCaseCandidate.size > 0){
			val newCaseEl:PosTags.PosElement = PosTags.PosElement(
				newCaseCandidate(0)("short"),
				newCaseCandidate(0)("long"),
				newCaseCandidate(0)("tag")
			)
			val newPos:PosTags.PartOfSpeech = {
				newCaseEl.short match {
					case "none" => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							currentPos.value.person,
							currentPos.value.number,
							currentPos.value.tense,
							currentPos.value.voice,
							currentPos.value.mood,
							currentPos.value.gender,
							None,
							currentPos.value.degree
						)
					}	
					case _ => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							currentPos.value.person,
							currentPos.value.number,
							currentPos.value.tense,
							currentPos.value.voice,
							currentPos.value.mood,
							currentPos.value.gender,
							Some(newCaseEl),
							currentPos.value.degree
						)
					}
				}
			}
			currentPos.value = newPos
		}
	}

	def updateCurrentPosWithGender(genderTag:String):Unit = {
		g.console.log(s"updating gender with ${genderTag}")
		val newGenderCandidate:Vector[Map[String,String]] = PosTags.genders.filter( _("tag") == genderTag )
		g.console.log(s"updating gender with ${newGenderCandidate}")
		if (newGenderCandidate.size > 0){
			val newGenderEl:PosTags.PosElement = PosTags.PosElement(
				newGenderCandidate(0)("short"),
				newGenderCandidate(0)("long"),
				newGenderCandidate(0)("tag")
			)
			val newPos:PosTags.PartOfSpeech = {
				newGenderEl.short match {
					case "none" => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							currentPos.value.person,
							currentPos.value.number,
							currentPos.value.tense,
							currentPos.value.voice,
							currentPos.value.mood,
							None,
							currentPos.value.grammaticalcase,
							currentPos.value.degree
						)
					}	
					case _ => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							currentPos.value.person,
							currentPos.value.number,
							currentPos.value.tense,
							currentPos.value.voice,
							currentPos.value.mood,
							Some(newGenderEl),
							currentPos.value.grammaticalcase,
							currentPos.value.degree
						)
					}
				}
			}
			currentPos.value = newPos
		}
	}

	def updateCurrentPosWithDegree(degreeTag:String):Unit = {
		val newDegreeCandidate:Vector[Map[String,String]] = PosTags.degrees.filter( _("tag") == degreeTag )
		if (newDegreeCandidate.size > 0){
			val newDegreeEl:PosTags.PosElement = PosTags.PosElement(
				newDegreeCandidate(0)("short"),
				newDegreeCandidate(0)("long"),
				newDegreeCandidate(0)("tag")
			)
			val newPos:PosTags.PartOfSpeech = {
				newDegreeEl.short match {
					case "none" => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							currentPos.value.person,
							currentPos.value.number,
							currentPos.value.tense,
							currentPos.value.voice,
							currentPos.value.mood,
							currentPos.value.gender,
							currentPos.value.grammaticalcase,
							None
						)
					}	
					case _ => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							currentPos.value.person,
							currentPos.value.number,
							currentPos.value.tense,
							currentPos.value.voice,
							currentPos.value.mood,
							currentPos.value.gender,
							currentPos.value.grammaticalcase,
							Some(newDegreeEl)
						)
					}
				}
			}
			currentPos.value = newPos
		}
	}

def updateCurrentPosWithTense(tenseTag:String):Unit = {
		val newTenseCandidate:Vector[Map[String,String]] = PosTags.tenses.filter( _("tag") == tenseTag )
		if (newTenseCandidate.size > 0){
			val newTenseEl:PosTags.PosElement = PosTags.PosElement(
				newTenseCandidate(0)("short"),
				newTenseCandidate(0)("long"),
				newTenseCandidate(0)("tag")
			)
			val newPos:PosTags.PartOfSpeech = {
				newTenseEl.short match {
					case "none" => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							currentPos.value.person,
							currentPos.value.number,
							None,
							currentPos.value.voice,
							currentPos.value.mood,
							currentPos.value.gender,
							currentPos.value.grammaticalcase,
							currentPos.value.degree
						)
					}	
					case _ => {
						PosTags.PartOfSpeech(
							currentPos.value.pos,
							currentPos.value.person,
							currentPos.value.number,
							Some(newTenseEl),
							currentPos.value.voice,
							currentPos.value.mood,
							currentPos.value.gender,
							currentPos.value.grammaticalcase,
							currentPos.value.degree
						)
					}
				}
			}
			currentPos.value = newPos

			
		}	
	}

	
	


}
