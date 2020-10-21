package de.westnordost.streetcomplete

import de.westnordost.osmapi.map.data.BoundingBox
import de.westnordost.streetcomplete.data.osm.osmquest.OsmElementQuestType
import de.westnordost.streetcomplete.data.osm.osmquest.OsmFilterQuestType
import de.westnordost.streetcomplete.data.osm.mapdata.OverpassMapDataAndGeometryApi
import de.westnordost.streetcomplete.data.osm.osmquest.OsmDownloaderQuestType
import de.westnordost.streetcomplete.quests.QuestModule
import de.westnordost.streetcomplete.settings.ResurveyIntervalsStore
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyInt

fun main() {

    val overpassMock: OverpassMapDataAndGeometryApi = mock()
    on(overpassMock.query(any(), any())).then { invocation ->
        var query = invocation.getArgument(0) as String
        // make query overpass-turbo friendly
        query = query
            .replace("0,0,1,1", "{{bbox}}")
            .replace("out meta geom 2000;", "out meta geom;")
        print("```\n$query\n```\n")
        true
    }

    val resurveyIntervalsStoreMock: ResurveyIntervalsStore = mock()
    on(resurveyIntervalsStoreMock.times(anyInt())).thenAnswer { (it.arguments[0] as Int).toDouble() }
    on(resurveyIntervalsStoreMock.times(ArgumentMatchers.anyDouble())).thenAnswer { (it.arguments[0] as Double) }

    val registry = QuestModule.questTypeRegistry(
        mock(), overpassMock, resurveyIntervalsStoreMock, mock(), mock(), mock(), mock()
    )

    val bbox = BoundingBox(0.0,0.0,1.0,1.0)

    for (questType in registry.all) {
        if (questType is OsmElementQuestType) {
            println("### " + questType.javaClass.simpleName)
            if (questType is OsmFilterQuestType) {
                val query = "[bbox:{{bbox}}];\n" + questType.filter.toOverpassQLString() + "\n out meta geom;"
                println("```\n$query\n```")
            } else if (questType is OsmDownloaderQuestType) {
                questType.download(bbox, mock())
            } else {
                println("Not available, see source code")
            }
            println()
        }
    }
}
