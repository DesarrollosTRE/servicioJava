/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista.Interfaces;

import java.util.function.Consumer;

/**
 *
 * @author PROGRAMADOR
 */
@FunctionalInterface
public interface ThrowingErrorLista<unObjeto> extends Consumer<unObjeto> {

    @Override
    default void accept(final unObjeto elem) {
        try {
            acceptThrows(elem);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    void acceptThrows(unObjeto elem) throws Exception;

}