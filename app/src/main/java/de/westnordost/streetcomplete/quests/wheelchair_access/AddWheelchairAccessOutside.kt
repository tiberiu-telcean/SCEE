package de.westnordost.streetcomplete.quests.wheelchair_access

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.geometry.ElementGeometry
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType
import de.westnordost.streetcomplete.data.user.achievements.EditTypeAchievement.RARE
import de.westnordost.streetcomplete.data.user.achievements.EditTypeAchievement.WHEELCHAIR
import de.westnordost.streetcomplete.osm.Tags
import de.westnordost.streetcomplete.osm.updateWithCheckDate

class AddWheelchairAccessOutside : OsmFilterQuestType<WheelchairAccess>() {

    override val elementFilter = """
        nodes, ways, relations with
         leisure = dog_park
         and access !~ no|private
         and (!wheelchair or wheelchair older today -8 years)
    """
    override val changesetComment = "Survey wheelchair accessibility of outside places"
    override val wikiLink = "Key:wheelchair"
    override val icon = R.drawable.ic_quest_toilets_wheelchair
    override val achievements = listOf(RARE, WHEELCHAIR)

    override fun getTitle(tags: Map<String, String>) = R.string.quest_wheelchairAccess_outside_title

    override fun createForm() = AddWheelchairAccessOutsideForm()

    override fun applyAnswerTo(answer: WheelchairAccess, tags: Tags, geometry: ElementGeometry, timestampEdited: Long) {
        tags.updateWithCheckDate("wheelchair", answer.osmValue)
        answer.updatedDescriptions?.forEach { (language, description) ->
            // language already contains the colon, or may be empty
            if (description.isEmpty())
                tags.remove("wheelchair:description$language")
            else
                tags["wheelchair:description$language"] = description
        }
    }
}
