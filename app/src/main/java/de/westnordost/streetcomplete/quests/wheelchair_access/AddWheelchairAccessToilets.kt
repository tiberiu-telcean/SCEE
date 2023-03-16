package de.westnordost.streetcomplete.quests.wheelchair_access

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.geometry.ElementGeometry
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType
import de.westnordost.streetcomplete.data.user.achievements.EditTypeAchievement.WHEELCHAIR
import de.westnordost.streetcomplete.osm.Tags

class AddWheelchairAccessToilets : OsmFilterQuestType<WheelchairAccessToiletsAnswer>() {

    override val elementFilter = """
        nodes, ways with amenity = toilets
         and access !~ no|private
         and (
           !wheelchair
           or wheelchair != yes and wheelchair older today -4 years
           or wheelchair older today -8 years
         )
    """
    override val changesetComment = "Specify wheelchair accessibility of toilets"
    override val wikiLink = "Key:wheelchair"
    override val icon = R.drawable.ic_quest_toilets_wheelchair
    override val isDeleteElementEnabled = true
    override val achievements = listOf(WHEELCHAIR)

    override fun getTitle(tags: Map<String, String>) = R.string.quest_wheelchairAccess_outside_title

    override fun createForm() = AddWheelchairAccessToiletsForm()

    override fun applyAnswerTo(answer: WheelchairAccessToiletsAnswer, tags: Tags, geometry: ElementGeometry, timestampEdited: Long) {
        answer.applyTo(tags)
        if (answer is WheelchairAccessToilets)
            answer.access.updatedDescriptions?.forEach { (language, description) ->
                // language already contains the colon, or may be empty
                if (description.isEmpty())
                    tags.remove("wheelchair:description$language")
                else
                    tags["wheelchair:description$language"] = description
            }
    }
}
