package jp.itohiro.sample.infra.reladomo

import com.gs.fw.common.mithra.MithraManagerProvider
import com.gs.fw.common.mithra.MithraTransaction
import com.gs.fw.common.mithra.TransactionalCommand
import jp.itohiro.sample.SampleTestConfiguration
import jp.itohiro.sample.domain.entity.Customer
import jp.itohiro.sample.domain.entity.CustomerFinder
import jp.itohiro.sample.domain.entity.Department
import jp.itohiro.sample.domain.entity.DepartmentFinder
import jp.itohiro.sample.util.DateUtils
import org.junit.jupiter.api.AfterEach
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

                val X部署 = Department(DateUtils.parse("202401"))
                X部署.departmentName = "X部署"
                X部署.departmentType = "営業部署"
                X部署.insert()

                val Y部署 = Department(DateUtils.parse("202401"))
                Y部署.departmentName = "Y部署"
                Y部署.departmentType = "営業支援部署"
                Y部署.insert()


                val Y部署2月以降 = DepartmentFinder.findOne(
                    DepartmentFinder.departmentName().eq("Y部署")
                        .and(DepartmentFinder.applicableRange().eq(DateUtils.parse("202402"))))

                Y部署2月以降.setDepartmentTypeUntil("営業部署", DateUtils.parse("202408"))


                val X部署1月以降 = DepartmentFinder.findOne(
                    DepartmentFinder.departmentName().eq("X部署")
                        .and(DepartmentFinder.applicableRange().eq(DateUtils.parse("202401"))))

                val A得意先1月以降 = Customer(DateUtils.parse("202401"))
                A得意先1月以降.customerName = "A得意先"
                A得意先1月以降.salesDepartment = X部署1月以降

                A得意先1月以降.insert()


                val A得意先2月以降 = CustomerFinder.findOne(
                    CustomerFinder.customerName().eq("A得意先")
                        .and(CustomerFinder.applicableRange().eq(DateUtils.parse("202402")))
                )
                A得意先2月以降.salesDepartment = Y部署2月以降

                val A得意先1月 = CustomerFinder.findOne(
                    CustomerFinder.customerName().eq("A得意先")
                        .and(CustomerFinder.applicableRange().eq(DateUtils.parse("202401")))
                )
                val A得意先2月 = CustomerFinder.findOne(
                    CustomerFinder.customerName().eq("A得意先")
                        .and(CustomerFinder.applicableRange().eq(DateUtils.parse("202402")))
                )
                val A得意先7月 = CustomerFinder.findOne(
                    CustomerFinder.customerName().eq("A得意先")
                        .and(CustomerFinder.applicableRange().eq(DateUtils.parse("202407")))
                )
                val A得意先8月 = CustomerFinder.findOne(
                    CustomerFinder.customerName().eq("A得意先")
                        .and(CustomerFinder.applicableRange().eq(DateUtils.parse("202408")))
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
                    DepartmentFinder.applicableRange().eq(DateUtils.parse("202401"))
                ).purgeAll()
                DepartmentFinder.findMany(
                    DepartmentFinder.applicableRange().eq(DateUtils.parse("202402"))
                ).purgeAll()
                DepartmentFinder.findMany(
                    DepartmentFinder.applicableRange().eq(DateUtils.parse("202403"))
                ).purgeAll()
                DepartmentFinder.findMany(
                    DepartmentFinder.applicableRange().eq(DateUtils.parse("202408"))
                ).purgeAll()

                CustomerFinder.findMany(
                    CustomerFinder.applicableRange().eq(DateUtils.parse("202401"))
                ).purgeAll()
                CustomerFinder.findMany(
                    CustomerFinder.applicableRange().eq(DateUtils.parse("202402"))
                ).purgeAll()
                CustomerFinder.findMany(
                    CustomerFinder.applicableRange().eq(DateUtils.parse("202403"))
                ).purgeAll()
                CustomerFinder.findMany(
                    CustomerFinder.applicableRange().eq(DateUtils.parse("202404"))
                ).purgeAll()
            })
    }
}