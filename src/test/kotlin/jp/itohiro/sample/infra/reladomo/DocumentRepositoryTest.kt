package jp.itohiro.sample.infra.reladomo

import com.gs.fw.common.mithra.MithraManagerProvider
import com.gs.fw.common.mithra.MithraTransaction
import com.gs.fw.common.mithra.TransactionalCommand
import jp.itohiro.sample.SampleTestConfiguration
import jp.itohiro.sample.domain.DocumentRepository
import jp.itohiro.sample.domain.entity.Document
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [SampleTestConfiguration::class])
internal class DocumentRepositoryTest (
    @Autowired private val documentRepository: DocumentRepository
) {

    @Test
    fun shouldFindAll() {
        val document = Document()
        document.title = "タイトル"
        document.content = "この内容は色々変わるんだけど、こんな感じね"
        MithraManagerProvider.getMithraManager()
            .executeTransactionalCommand<Any>(TransactionalCommand<Any> { tx: MithraTransaction? ->
                document.insert()
                document
            })

    }
}