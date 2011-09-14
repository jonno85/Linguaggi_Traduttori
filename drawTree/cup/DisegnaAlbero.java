import java.util.ArrayList;
import java.awt.*;


public class DisegnaAlbero
{

	ArrayList stack;
	double ultimo_inserimento;
	double deltaX;
	double deltaY;
	double raggio;
	sym term;
	Font ff;
	double numE;

	public DisegnaAlbero()
	{
		stack = new ArrayList();
		term = new sym();
		ultimo_inserimento = 0;

		//valori predefiniti
		deltaX = .01;
		deltaY = .022;
		raggio = .006;
		double CsizeX = 800;
		double CsizeY = 600;
		StdDraw.setCanvasSize(3000, 2000);
		ff = new Font("DialogInput", Font.BOLD, 9);
		StdDraw.setFont(ff);
		numE = 0;
	}




	public void push(String string, int pp)
	{
		String nomeP;
		if (numE == 0)
			numE = .01;
		else
			numE = 0;
		string = string.substring(1);
		if (pp == 1) nomeP = new String(string);
		else nomeP = new String(term.getTT(new Integer(string).intValue()));
		//System.out.println("PUSH:" + string);
		if(pp==1)
			StdDraw.setPenColor(StdDraw.MAGENTA );
		else
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
		if (stack.isEmpty() == true)
		{
			stack.add(0, new Nodo(string, 0, numE));
			StdDraw.filledCircle(0, numE, raggio);
			StdDraw.setPenColor();
			StdDraw.text(0, numE, nomeP);
		}
		else
		{
			stack.add(0, new Nodo(string, ultimo_inserimento + deltaX, numE));
			StdDraw.filledCircle(ultimo_inserimento + deltaX, numE, raggio);
			StdDraw.setPenColor();
			StdDraw.text(ultimo_inserimento + deltaX, numE, nomeP);//term.getTT(new Integer(string).intValue()));
			ultimo_inserimento += deltaX;
		}



	}



	public void reduce(int i, String string, int fine)
	{
		if (i == 0) push(string, 1);
		else
		{
			//System.out.println("REDUCE:" + i + string + fine);
			Nodo tempN = (Nodo)stack.get(0); //tolgo elemento in testa
			stack.remove(0);
			double maxY = ((Nodo)stack.get(0)).posY, X = ((Nodo)stack.get(0)).posX, posXX = 0;

			//ciclo per prendere posizione Y massima
			for (int j = 0; j < i; j++)
			{
				if (((Nodo)stack.get(j)).posY > maxY)
					maxY = ((Nodo)stack.get(j)).posY;
				posXX += ((Nodo)stack.get(j)).posX;

			}
			posXX = posXX / i;
			//ciclo per disegnare linee di congiunzione
			for (int j = 0; j < i; j++)
			{
				StdDraw.line(((Nodo)stack.get(j)).posX, ((Nodo)stack.get(j)).posY + raggio, posXX, maxY + deltaY - raggio);

			}

			//ciclo per fare pop elementi
			for (int j = 0; j < i; j++)
			{
				stack.remove(0);
			}
			stack.add(0, new Nodo(string, posXX, maxY + deltaY));

			//setta colore, rosa se ultima riduzione
			if (fine == 0)
				StdDraw.setPenColor(StdDraw.GREEN);
			else
				StdDraw.setPenColor(StdDraw.PINK);

			StdDraw.filledCircle(posXX, maxY + deltaY, raggio);
			StdDraw.setPenColor();
			StdDraw.text(posXX, maxY + deltaY, string);
			stack.add(0, tempN); //riaggiungo elemnto in testa

		}

	}

}