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
import edu.holycross.shot.greek._
import edu.holycross.shot.citeobj._
import scala.scalajs.js.annotation.JSExport
import js.annotation._


@JSExportTopLevel("posTagger.MainView")
object MainView {

	//val textView = O2View.o2div




	@dom
	def mainMessageDiv = {
			<div id="main_message" class={ s"app_message ${MainModel.userMessageVisibility.bind} ${MainModel.userAlert.bind}" } >
				<p> { MainModel.userMessage.bind }  </p>
			</div>
	}

	@dom
	def typingDiv = {
		val greekKeyUpHandler = { event: KeyboardEvent =>
			(event.currentTarget, event.keyCode) match {
				case(input: HTMLTextAreaElement, _) =>  {
					//O2Controller.validateUrn(s"${input.value.toString}")
					g.console.log(s"${input.value.toString}")
					g.console.log(s"${MainController.greekify(input.value.toString)}")	
					js.Dynamic.global.document.getElementById("greekOutput").innerHTML = MainController.greekify(input.value.toString)
				}
				case _ =>
			}
		}

		<textarea
		class={ s"greekInputField" }
		id="greekInput"
		cols={ 40 }
		rows={ 5 }
		value=""
		onkeyup={ greekKeyUpHandler }>
		</textarea>	

		<div id="greekOutput"></div>

	}


	@dom
	def mainDiv = {
		<div id="main-wrapper">
		<header>
			Part of Speech Tag Generator (Greek)
			<span id="app_header_versionInfo">version { BuildInfo.version }</span>
		</header>

		<article id="main_Container">

			
			{ posMenu.bind }
			{ moodMenu.bind }
			{ voiceMenu.bind }
			{ tenseMenu.bind }
			{ personMenu.bind }
			{ genderMenu.bind }
			{ caseMenu.bind }
			{ numberMenu.bind }
			{ degreeMenu.bind }

			{ currentPosDiv.bind }
			{ mainMessageDiv.bind }

			{ bigDisplay.bind }

			<!-- 
			{ typingDiv.bind }
			-->

		</article>
		 <div class="push"></div>
		<footer>
		{ footer.bind }
		</footer>
	</div>
	}

	@dom
	def currentPosDiv = {
		<div id="currentPos">
			<p>
				{ MainModel.currentPos.bind.toString }
			</p>
		</div>
	}

	@dom
	def posMenu = {
		<label for="posMenu">Part of Speech</label>
		<select id="posMenu"
			onchange={ event: Event => {
				val thisTarget = event.target.asInstanceOf[org.scalajs.dom.raw.HTMLSelectElement]
				val testText:String = thisTarget.value.toString
				MainModel.updateCurrentPosWithPartOfSpeech(testText)
			}} >
			{ posMenuOptions.bind }
		</select>	
	}

	@dom
	def posMenuOptions = {
		{
			for (c <- MainModel.boundPartsOfSpeech) yield {
				<option value={ c("tag") } >{ c("long") }</option>
			}
		}
	}

	@dom
	def moodMenu = {
		<label for="moodMenu"
			class={
				MainModel.boundMoods.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}
		>Mood</label>
		<select id="moodMenu"
			class={
				MainModel.boundMoods.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}
			onchange={ event: Event => {
				val thisTarget = event.target.asInstanceOf[org.scalajs.dom.raw.HTMLSelectElement]
				val testText:String = thisTarget.value.toString
				MainModel.updateCurrentPosWithMood(testText)
			}} >
			{ moodMenuOptions.bind }
		</select>	
	}

	@dom
	def moodMenuOptions = {
		{
			for (c <- MainModel.boundMoods) yield {
				<option value={ c.tag } >{ c.long }</option>
			}
		}
	}

	@dom
	def voiceMenu = {
		<label for="voiceMenu"
			class={
				MainModel.boundVoices.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}
		>Voice</label>
		<select id="voiceMenu"
			class={
				MainModel.boundVoices.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}
			onchange={ event: Event => {
				val thisTarget = event.target.asInstanceOf[org.scalajs.dom.raw.HTMLSelectElement]
				val testText:String = thisTarget.value.toString
				MainModel.updateCurrentPosWithVoice(testText)
			}} >
			{ voiceMenuOptions.bind }
		</select>	
	}

	@dom
	def voiceMenuOptions = {
		{
			for (c <- MainModel.boundVoices) yield {
				<option value={ c.tag } >{ c.long }</option>
			}
		}
	}

	@dom
	def tenseMenu = {
		<label for="tenseMenu"
			class={
				MainModel.boundTenses.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}
		>Tense</label>
		<select id="tenseMenu"
			class={
				MainModel.boundTenses.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}
			onchange={ event: Event => {
				val thisTarget = event.target.asInstanceOf[org.scalajs.dom.raw.HTMLSelectElement]
				val testText:String = thisTarget.value.toString
				MainModel.updateCurrentPosWithTense(testText)
			}} >
			{ tenseMenuOptions.bind }
		</select>	
	}

	@dom
	def tenseMenuOptions = {
		{
			for (c <- MainModel.boundTenses) yield {
				<option value={ c.tag } >{ c.long }</option>
			}
		}
	}

	@dom
	def personMenu = {
		<label for="personMenu"
			class={
				MainModel.boundPersons.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}
		>Person</label>
		<select id="personMenu"
			class={
				MainModel.boundPersons.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}
			onchange={ event: Event => {
				val thisTarget = event.target.asInstanceOf[org.scalajs.dom.raw.HTMLSelectElement]
				val testText:String = thisTarget.value.toString
				MainModel.updateCurrentPosWithPerson(testText)
			}} >
			{ personMenuOptions.bind }
		</select>	
	}

	@dom
	def personMenuOptions = {
		{
			for (c <- MainModel.boundPersons) yield {
				<option value={ c.tag } >{ c.long }</option>
			}
		}
	}

	@dom
	def numberMenu = {
		<label for="numberMenu"
			class={
				MainModel.boundNumbers.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}

		>Number</label>
		<select id="numberMenu"
			class={
				MainModel.boundNumbers.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}

			onchange={ event: Event => {
				val thisTarget = event.target.asInstanceOf[org.scalajs.dom.raw.HTMLSelectElement]
				val testText:String = thisTarget.value.toString
				MainModel.updateCurrentPosWithNumber(testText)
			}} >
			{ numberMenuOptions.bind }
		</select>	
	}

	@dom
	def numberMenuOptions = {
		{
			for (c <- MainModel.boundNumbers) yield {
				<option value={ c.tag } >{ c.long }</option>
			}
		}
	}

	@dom
	def caseMenu = {
		<label for="caseMenu"
			class={
				MainModel.boundCases.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}
		
		>Case</label>
		<select id="caseMenu"
			class={
				MainModel.boundCases.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}

			onchange={ event: Event => {
				val thisTarget = event.target.asInstanceOf[org.scalajs.dom.raw.HTMLSelectElement]
				val testText:String = thisTarget.value.toString
				MainModel.updateCurrentPosWithCase(testText)
			}} >
			{ caseMenuOptions.bind }
		</select>	
	}

	@dom
	def caseMenuOptions = {
		{
			for (c <- MainModel.boundCases) yield {
				<option value={ c.tag } >{ c.long }</option>
			}
		}
	}

	@dom
	def genderMenu = {
		<label for="genderMenu"
			class={
				MainModel.boundGenders.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}

		>Gender</label>
		<select id="genderMenu"
			class={
				MainModel.boundGenders.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}

			onchange={ event: Event => {
				val thisTarget = event.target.asInstanceOf[org.scalajs.dom.raw.HTMLSelectElement]
				val testText:String = thisTarget.value.toString
				MainModel.updateCurrentPosWithGender(testText)
			}} >
			{ genderMenuOptions.bind }
		</select>	
	}

	@dom
	def genderMenuOptions = {
		{
			for (c <- MainModel.boundGenders) yield {
				<option value={ c.tag } >{ c.long }</option>
			}
		}
	}

	@dom
	def degreeMenu = {
		<label for="degreeMenu"
			class={
				MainModel.boundDegrees.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}

		>Degree</label>
		<select id="degreeMenu"
			class={
				MainModel.boundDegrees.length.bind match {
					case n if (n > 1) => "app_visible"
					case _ => "app_hidden"
				}		
			}

			onchange={ event: Event => {
				val thisTarget = event.target.asInstanceOf[org.scalajs.dom.raw.HTMLSelectElement]
				val testText:String = thisTarget.value.toString
				MainModel.updateCurrentPosWithDegree(testText)
			}} >
			{ degreeMenuOptions.bind }
		</select>	
	}

	@dom
	def degreeMenuOptions = {
		{
			for (c <- MainModel.boundDegrees) yield {
				<option value={ c.tag } >{ c.long }</option>
			}
		}
	}

	@dom
	def bigDisplay = {
		<div class="bigPosTag">
			<textarea id="hiddenTextArea"></textarea>
			<a class="bigPosTag"
			  onclick={ event: Event => {
				val thisTarget = event.target.asInstanceOf[org.scalajs.dom.raw.HTMLAnchorElement]
				val selectedText:String = thisTarget.text
			    val hiddenTextArea = js.Dynamic.global.document.getElementById("hiddenTextArea")
			    hiddenTextArea.textContent = selectedText.trim
			    hiddenTextArea.select()
			    val successful = document.execCommand("copy")
				successful match {
					case true => {
						val message:String = s"""Copied "${selectedText}" to clipboard."""
						MainController.updateUserMessage(message, 0)
					}
					case false => {
						val message:String = s"""Failed to copy "${selectedText}" to clipboard."""
						MainController.updateUserMessage(message, 2)
					}
				}
			}}	>
			{ MainModel.currentPos.bind.toPosTag }
			</a>
			<span class="smallPosTag"> (click to copy) </span>
		</div>
	}



	@dom
	def footer = {
		<p>
		POSTagger(Greek) was written in 2018 by Christopher Blackwell and licensed CC0 (Public Domain). Source and issue-tracker at <a href="https://github.com/Eumaeus/pos-tagger">https://github.com/Eumaeus/pos-tagger</a>. 
		</p>
	}


}
