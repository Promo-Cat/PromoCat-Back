package org.promocat.promocat.utils.soap.operations;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс, от которого должны наследоваться ВСЕ POJO, описывающие SOAP операции.
 * Каждое поле унаследованного класса соответствует параметрам операции
 * Так же важно то, что ИМЯ наследуемого класса должно быть точно таким же как название SOAP операции.
 * Каждое поле, которое должно будет сериализоваться/десериализоваться в xml - должно быть помечено аннотацией {@link org.promocat.promocat.constraints.XmlField}
 */
@Data
public abstract class AbstractOperation {

    public abstract Class<? extends AbstractOperation> getResponseClass();

}
