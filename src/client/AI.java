package client;

import client.model.Game;
import client.model.Node;

import java.util.Random;

/**
 * AI class.
 * You should fill body of the method {@link #doTurn}.
 * Do not change name or modifiers of the methods or fields
 * and do not add constructor for this class.
 * You can add as many methods or fields as you want!
 * Use world parameter to access and modify game's
 * world!
 * See World interface for more details.
 */
public class AI {

    public void doTurn(Game game) {
        // fill this method, we've presented a stupid AI for example!
        Random rand = new Random();


        if (game.getCurrentTurn() == 0)
        {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 3; k++) {
                        game.changeStrategy(0, i, j, k,rand.nextInt(2));
                        game.changeStrategy(1, i, j, k,rand.nextInt(2));
                    }
                }
            }
        }
        else
        {
            // no strategy change
        }

    }

}
