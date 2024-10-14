package Fish;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// fishdata table CRUD
public class FishDAO {
	
	private String username="root";
	private String password="11111111";
	private String url="jdbc:oracle:thin:@localhost:1521:orcl";
	private String drivername="oracle.jdbc.driver.OracleDriver";
	private Connection conn = null;	// 커넥션 자원 변수
	
	public static FishDAO fishdao=null;	// 자기 자신의 객체 주소 변수
	private FishDAO (){
		init();
	}
	public static FishDAO getInstance() {
		if(fishdao == null) {
			fishdao = new FishDAO();
		}
		return fishdao;
	}
	
	private void init() {// 드라이버 로드
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("오라클 드라이버 로드 성공");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private boolean conn() { // 커넥션 가져오는 공통 코드를 메서드로 정의

		try {
			conn = DriverManager.getConnection
				("jdbc:oracle:thin:@localhost:1521:orcl",
						"system", //아이디
						"11111111");
			System.out.println("커넥션 자원 획득 성공");
			return true;	// 커넥션 자원을 정상적으로 획득 할시
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;		// 커넥션 자원획득 실패시
	}
	
	public void update(FishDTO fdto) {
		if(conn()) {
			try {
				String sql= "update fishdata set pwd=? where id=?";
				PreparedStatement psmt = conn.prepareStatement(sql);
				psmt.setString(1, fdto.getPwd());
				psmt.setString(2, fdto.getId());
				psmt.executeUpdate();
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				
			}
		}
	}
	
	public void delete(String delId) {
		if(conn()) {
			try {
				String sql = "delete from fishdata where id=?";
				PreparedStatement psmt = conn.prepareStatement(sql);
				psmt.setString(1, delId);
				psmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				try {
					if(conn != null) conn.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
	}
	
	public FishDTO selecOne(String findId) {
		if(conn()) {
			try {
				String sql= "select * from fishdata where id=?";
				PreparedStatement psmt = conn.prepareStatement(sql);
				psmt.setString(1, findId);
				ResultSet rs = psmt.executeQuery();
				if(rs.next()) {
					FishDTO fishTemp = new FishDTO();
					fishTemp.setId(rs.getString("id"));
					fishTemp.setPwd(rs.getString("pwd"));
					fishTemp.setIndate(rs.getString("indate"));
					return fishTemp;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
		
	}
	
	public ArrayList<FishDTO> selectAll(/*매핑할필요없음으로빈칸*/){
		ArrayList<FishDTO> flist = new ArrayList<FishDTO>();
		if(conn()) {
			try {
				String sql = "select * from fishdata";
				PreparedStatement psmt = conn.prepareStatement(sql);
				ResultSet rs = psmt.executeQuery();
				// ResultSet은 테이블 형식으로 가져온다고 이해합니다.
				while(rs.next()) {	//next() 메서드는 rs에서 참조하는 테이블에서 
									// 튜플을 순차적으로 하나씩 접근하는 메서드
					FishDTO fishTemp= new FishDTO();
					// rs.getString("id") rs가 접근한 튜플에서 id컬럽의 값을 가져옴
					fishTemp.setId(rs.getString("id"));
					fishTemp.setPwd(rs.getString("pwd"));
					fishTemp.setIndate(rs.getString("indate"));
					flist.add(fishTemp);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return flist;	
	}
	
	public void add(FishDTO fdto) {
		if(conn()) {
			try {
				String sql = "insert into fishdata values (?,?,default)";	//java 입장에선 그냥 문자열일뿐!
				PreparedStatement psmt = conn.prepareStatement(sql);	
				psmt.setString(1, fdto.getId());
				psmt.setString(2, fdto.getPwd());
				// 쿼리실행
//				psmt.executeUpdate();
				// 쿼리 실행후 리턴을 받아서 다음 처리 작업 정의  / 선택 트렌젝션?처리
				int resultInt = psmt.executeUpdate();
				if(resultInt > 0) {
					conn.commit();	// 현재 작업 저장
				}else {
					conn.rollback();	// 현재 작업 취소 
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}else {
			System.out.println("데이터베이스 커넥션 실패");
		}
				
	}
	
	
	
	
}
