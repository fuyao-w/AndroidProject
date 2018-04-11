package DBUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	/**
	 * 执行查询操作
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet query(String sql) throws SQLException {
		Connection connection = ConUtil.getConn();
		Statement statement;
		if (connection != null) {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			return rs;
		}
		return null;
	}

	/**
	 * 对数据库进行修改
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static int update(String sql) throws SQLException {
		Connection connection = ConUtil.getConn();
		Statement statement;
		if (connection != null) {
			statement = connection.createStatement();
			int result = statement.executeUpdate(sql);
			return result;
		}
		return 0;
	}
}
