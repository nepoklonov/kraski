package kraski.server.database

import kraski.common.interpretation.ScaleType
import kraski.common.interpretation.put
import kraski.common.interpretation.x
import kraski.common.models.News
import kraski.common.models.NewsWithSrc
import kraski.common.models.Raskraska
import kraski.common.models.RaskraskaWithSrc
import kraski.common.models.Stories
import kraski.common.models.StoriesWithSrc
import kraski.common.models.Review
import org.jetbrains.exposed.sql.SortOrder

fun getAllNews(width: Int, height: Int): List<NewsWithSrc> = loggedTransaction {
    News::class.getModelTable().let { table ->
        table.selectAllModelsOrderBy(table[News::date], SortOrder.DESC).map { news ->
            val src = if (news.imageFileId != -1 && news.imageFileId != Int.MIN_VALUE) {
                getImageVersion(news.imageFileId, width x height put ScaleType.OUTSIDE)
            } else ""
            NewsWithSrc(news, src)
        }
    }
}
fun getAllRaskraska(width: Int, height: Int): List<RaskraskaWithSrc> {
    return loggedTransaction {
        Raskraska::class.getModelTable().let { table ->
            table.selectAllModelsOrderBy(table[Raskraska::header], SortOrder.DESC).map { raskraska ->
                val src = if (raskraska.imageFileId != -1 && raskraska.imageFileId != Int.MIN_VALUE) {
                    getImageVersion(raskraska.imageFileId, width x height put ScaleType.OUTSIDE)
                } else ""
                RaskraskaWithSrc(raskraska, src)
            }
        }
    }
}
fun getAllStories(width: Int, height: Int): List<StoriesWithSrc> {
    return loggedTransaction {
        Stories::class.getModelTable().let { table ->
            table.selectAllModelsOrderBy(table[Stories::header], SortOrder.DESC).map { stories ->
                val src = ""
                StoriesWithSrc(stories, src)
            }
        }
    }
}

fun getAllReview(): List<Review> = loggedTransaction {
    Review::class.getModelTable().selectAllModels()
}