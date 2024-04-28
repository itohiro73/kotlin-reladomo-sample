package jp.itohiro.sample.infra.util

import com.gs.fw.common.mithra.MithraSequence
import com.gs.fw.common.mithra.MithraSequenceObjectFactory
import jp.itohiro.sample.domain.entity.ObjectSequence
import jp.itohiro.sample.domain.entity.ObjectSequenceFinder


class ObjectSequenceObjectFactory : MithraSequenceObjectFactory {
    override fun getMithraSequenceObject(
        sequenceName: String,
        sourceAttribute: Any?,
        initialValue: Int
    ): MithraSequence {
        var objectSequence = ObjectSequenceFinder.findByPrimaryKey(sequenceName)
        if (objectSequence == null) {
            objectSequence = ObjectSequence()
            objectSequence.simulatedSequenceName = sequenceName
            objectSequence.nextId = initialValue.toLong()
            objectSequence.insert()
        }
        return objectSequence
    }
}