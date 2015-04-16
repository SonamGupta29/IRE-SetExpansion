

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import concept.Runner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Result
 */
@WebServlet("/Result")
public class Result extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Result() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int nResults = Integer.parseInt(request.getParameter("results"));
		String seedTerms = request.getParameter("seedTerms");
		String seeds[] = seedTerms.split("\n");
		for(int i=0; i<seeds.length; i++){
			seeds[i] = seeds[i].trim();
			if(seeds[i].contains("\n")){
				seeds[i] = seeds[i].replace("\n", "");
			}
		}
		ArrayList<String> seedList = new ArrayList<String>(Arrays.asList(seeds));
		System.out.println(seedList);
		ArrayList<String> output = Runner.getResults(seedList, nResults);
		request.setAttribute("output", output);
		getServletConfig().getServletContext().getRequestDispatcher("/index.jsp").forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
