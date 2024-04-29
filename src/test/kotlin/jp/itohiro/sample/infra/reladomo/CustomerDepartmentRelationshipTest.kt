package jp.itohiro.sample.infra.reladomo

import com.gs.fw.common.mithra.MithraManagerProvider
import com.gs.fw.common.mithra.MithraTransaction
import com.gs.fw.common.mithra.TransactionalCommand
import jp.itohiro.sample.SampleTestConfiguration
import jp.itohiro.sample.domain.entity.Customer
import jp.itohiro.sample.domain.entity.CustomerFinder
import jp.itohiro.sample.domain.entity.Department
import jp.itohiro.sample.domain.entity.DepartmentFinder
import jp.itohiro.sample.util.DateUtils.適用年月
import jp.itohiro.sample.util.DateUtils.適用終了年月
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [SampleTestConfiguration::class])
class CustomerDepartmentRelationshipTest {

    @BeforeEach
    fun setUp(): Unit {
        cleanUp()
    }

    @Test
    fun customerDepartmentScenario() {
        MithraManagerProvider.getMithraManager()
            .executeTransactionalCommand<Any>(TransactionalCommand<Any> { tx: MithraTransaction? ->

                val X部署 = Department(適用年月("202401"))
                X部署.departmentName = "X部署"
                X部署.departmentType = "営業部署"
                X部署.insert()


                // ReladomoのY部署オブジェクトを2024年1月付で作成
                val Y部署 = Department(適用年月("202401"))
                Y部署.departmentName = "Y部署"
                Y部署.departmentType = "営業支援部署"

                // この時点でデータベースに保存され、Y部署は2024年1月から永年で「営業支援部署」の部署タイプ情報が保存されます
                Y部署.insert()


                // Y部署の2024年2月以降の情報を変更するため、2024年2月時点でのY部署の情報を取得します
                val Y部署2月以降 = DepartmentFinder.findOne(
                    DepartmentFinder.departmentName().eq("Y部署")
                        .and(DepartmentFinder.applicableRange().eq(適用年月("202402"))))


                // 2024年2月から2024年7月までの間、Y部署の部署タイプを「営業部署」に変更します
                // 2024年8月からは元々の「営業支援部署」タイプが保持されます
                Y部署2月以降.setDepartmentTypeUntil("営業部署", 適用終了年月("202407"))


                val X部署1月以降 = DepartmentFinder.findOne(
                    DepartmentFinder.departmentName().eq("X部署")
                        .and(DepartmentFinder.applicableRange().eq(適用年月("202401"))))

                val A得意先1月以降 = Customer(適用年月("202401"))
                A得意先1月以降.customerName = "A得意先"
                A得意先1月以降.salesDepartment = X部署1月以降

                A得意先1月以降.insert()


                val A得意先2月以降 = CustomerFinder.findOne(
                    CustomerFinder.customerName().eq("A得意先")
                        .and(CustomerFinder.applicableRange().eq(適用年月("202402")))
                )
                A得意先2月以降.salesDepartment = Y部署2月以降

                val A得意先1月 = CustomerFinder.findOne(
                    CustomerFinder.customerName().eq("A得意先")
                        .and(CustomerFinder.applicableRange().eq(適用年月("202401")))
                )
                val A得意先2月 = CustomerFinder.findOne(
                    CustomerFinder.customerName().eq("A得意先")
                        .and(CustomerFinder.applicableRange().eq(適用年月("202402")))
                )
                val A得意先7月 = CustomerFinder.findOne(
                    CustomerFinder.customerName().eq("A得意先")
                        .and(CustomerFinder.applicableRange().eq(適用年月("202407")))
                )
                val A得意先8月 = CustomerFinder.findOne(
                    CustomerFinder.customerName().eq("A得意先")
                        .and(CustomerFinder.applicableRange().eq(適用年月("202408")))
                )

                assertEquals("X部署", A得意先1月.salesDepartment.departmentName)
                assertEquals("営業部署", A得意先1月.salesDepartment.departmentType)
                assertEquals("Y部署", A得意先2月.salesDepartment.departmentName)
                assertEquals("営業部署", A得意先2月.salesDepartment.departmentType)
                assertEquals("Y部署", A得意先7月.salesDepartment.departmentName)
                assertEquals("営業部署", A得意先7月.salesDepartment.departmentType)
                assertEquals("Y部署", A得意先8月.salesDepartment.departmentName)
                assertEquals("営業支援部署", A得意先8月.salesDepartment.departmentType)
            })
    }

//    @AfterEach
//    fun tearDown(): Unit {
//        cleanUp()
//    }

    private fun cleanUp() {
        MithraManagerProvider.getMithraManager()
            .executeTransactionalCommand<Any>(TransactionalCommand<Any> { tx: MithraTransaction? ->
                DepartmentFinder.findMany(
                    DepartmentFinder.applicableRange().eq(適用年月("202401"))
                ).purgeAll()
                DepartmentFinder.findMany(
                    DepartmentFinder.applicableRange().eq(適用年月("202402"))
                ).purgeAll()
                DepartmentFinder.findMany(
                    DepartmentFinder.applicableRange().eq(適用年月("202403"))
                ).purgeAll()
                DepartmentFinder.findMany(
                    DepartmentFinder.applicableRange().eq(適用年月("202408"))
                ).purgeAll()

                CustomerFinder.findMany(
                    CustomerFinder.applicableRange().eq(適用年月("202401"))
                ).purgeAll()
                CustomerFinder.findMany(
                    CustomerFinder.applicableRange().eq(適用年月("202402"))
                ).purgeAll()
                CustomerFinder.findMany(
                    CustomerFinder.applicableRange().eq(適用年月("202403"))
                ).purgeAll()
                CustomerFinder.findMany(
                    CustomerFinder.applicableRange().eq(適用年月("202404"))
                ).purgeAll()
            })
    }
}