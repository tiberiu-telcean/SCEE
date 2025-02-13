package de.westnordost.streetcomplete.data.quest

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import de.westnordost.streetcomplete.StreetCompleteApplication
import de.westnordost.streetcomplete.data.osm.edits.EditType
import de.westnordost.streetcomplete.quests.AbstractQuestForm

/** A quest type appears as a pin with an icon on the map and when opened, the quest type's
 *  question is displayed along with a UI to answer that quest.
 *
 *  How many quests of which types have been solved is persisted for the statistics and each quest
 *  type can contribute to unlocking new achievement levels of certain types.
 *
 *  Most QuestType inherit from [OsmElementQuestType][de.westnordost.streetcomplete.data.osm.osmquests.OsmElementQuestType] */
interface QuestType : EditType {

    val prefs: SharedPreferences get() = StreetCompleteApplication.preferences
    /** the string resource id that explains why this quest is disabled by default or zero if it is
     *  not disabled by default.
     *
     *  Quests that do not fully fulfill the [quest guidelines](https://github.com/streetcomplete/StreetComplete/blob/master/QUEST_GUIDELINES.md),
     *  (e.g. often the requirement that the information is publicly available from the outside),
     *  are disabled by default. */
    val defaultDisabledMessage: Int get() = 0

    /** returns the fragment in which the user can add the data */
    fun createForm(): AbstractQuestForm

    /** The quest type can clean it's metadata that is older than the given timestamp here, if any  */
    fun deleteMetadataOlderThan(timestamp: Long) {}

    /** if the quest should only be shown during day-light os night-time hours */
    val dayNightCycle: DayNightCycle get() = DayNightCycle.DAY_AND_NIGHT

    fun getQuestSettingsDialog(context: Context): AlertDialog? = null
    val hasQuestSettings: Boolean get() = false

    /** color of the dot, which is used instead of a quest pin */
    val dotColor: String? get() = null
}
