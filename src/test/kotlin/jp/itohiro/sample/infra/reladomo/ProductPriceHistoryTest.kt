package jp.itohiro.sample.infra.reladomo

import com.gs.fw.common.mithra.MithraManagerProvider
import jp.itohiro.sample.SampleTestConfiguration
import jp.itohiro.sample.domain.entity.Product
import jp.itohiro.sample.domain.entity.ProductFinder
import jp.itohiro.sample.util.DateUtils.currentTimestamp
import jp.itohiro.sample.util.DateUtils.適用年月
import jp.itohiro.sample.util.DateUtils.適用終了年月
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [SampleTestConfiguration::class])
class ProductPriceHistoryTest {

    @BeforeEach
    fun setUp() {
        cleanUp()
    }

    @Test
    fun productPriceHistoryScenario() {

        MithraManagerProvider.getMithraManager()
            .executeTransactionalCommand {
                // ReladomoのA商品オブジェクトを2024年1月付で作成
                val A商品 = Product(適用年月("202401"))
                A商品.productName = "A商品"
                A商品.price = 100
                // この時点でデータベースに保存され、A商品は2024年1月から永年で100円の価格情報が保存されます
                A商品.insert()

                // A商品の2024年2月以降の情報を変更するため、2024年2月時点でのA商品の情報を取得します
                val A商品2月以降 = ProductFinder.findOne(
                    ProductFinder.productName().eq("A商品")
                        .and(ProductFinder.validTime().eq(適用年月("202402"))))
                // 2024年2月以降のA商品の価格を110円に変更します。この時点では、2月以降の全ての価格情報が110円に変更されます
                A商品2月以降.price = 110

                // A商品の2024年3月以降の情報を変更するため、2024年3月時点でのA商品の情報を取得します
                val A商品3月以降 = ProductFinder.findOne(
                    ProductFinder.productName().eq("A商品")
                        .and(ProductFinder.validTime().eq(適用年月("202403"))))
                // 2024年2月のA商品の価格を12円に変更します
                A商品3月以降.price = 120

            }

        val 変更前タイムスタンプ = currentTimestamp

        MithraManagerProvider.getMithraManager()
            .executeTransactionalCommand {
                val A商品2月再取得 = ProductFinder.findOneBypassCache(
                    ProductFinder.productName().eq("A商品")
                        .and(ProductFinder.validTime().eq(適用年月("202402"))))

                // 2024年2月のA商品の価格を115円に変更します
                A商品2月再取得.setPriceUntil(115, 適用終了年月("202402"))
            }

        val A商品1月変更前 = ProductFinder.findOne(
            ProductFinder.productName().eq("A商品")
                .and(ProductFinder.validTime().eq(適用年月("202401")))
                .and(ProductFinder.transactionTime().eq(変更前タイムスタンプ))
                .and(ProductFinder.transactionTime().equalsEdgePoint())
        )

        val A商品2月変更前 = ProductFinder.findOne(
            ProductFinder.productName().eq("A商品")
                .and(ProductFinder.validTime().eq(適用年月("202402")))
                .and(ProductFinder.transactionTime().eq(変更前タイムスタンプ))
                .and(ProductFinder.transactionTime().equalsEdgePoint())
        )

        val A商品3月変更前 = ProductFinder.findOne(
            ProductFinder.productName().eq("A商品")
                .and(ProductFinder.validTime().eq(適用年月("202403")))
                .and(ProductFinder.transactionTime().eq(変更前タイムスタンプ))
                .and(ProductFinder.transactionTime().equalsEdgePoint())
        )

        val A商品1月最新 = ProductFinder.findOne(
            ProductFinder.productName().eq("A商品")
                .and(ProductFinder.validTime().eq(適用年月("202401")))
        )

        val A商品2月最新 = ProductFinder.findOne(
            ProductFinder.productName().eq("A商品")
                .and(ProductFinder.validTime().eq(適用年月("202402")))
        )

        val A商品3月最新 = ProductFinder.findOne(
            ProductFinder.productName().eq("A商品")
                .and(ProductFinder.validTime().eq(適用年月("202403")))
        )

        assertEquals(100, A商品1月変更前.price)
        assertEquals(110, A商品2月変更前.price)
        assertEquals(120, A商品3月変更前.price)

        assertEquals(100, A商品1月最新.price)
        assertEquals(115, A商品2月最新.price)
        assertEquals(120, A商品3月最新.price)
    }

//    @AfterEach
//    fun tearDown(): Unit {
//        cleanUp()
//    }

    private fun cleanUp() {
        MithraManagerProvider.getMithraManager()
            .executeTransactionalCommand {
                ProductFinder.findMany(
                    ProductFinder.validTime().eq(適用年月("202401")).and(
                        ProductFinder.transactionTime().equalsEdgePoint()
                    )
                ).purgeAll()
                ProductFinder.findMany(
                    ProductFinder.validTime().eq(適用年月("202402")).and(
                        ProductFinder.transactionTime().equalsEdgePoint()
                    )
                ).purgeAll()
                ProductFinder.findMany(
                    ProductFinder.validTime().eq(適用年月("202403")).and(
                        ProductFinder.transactionTime().equalsEdgePoint()
                    )
                ).purgeAll()
            }
    }
}