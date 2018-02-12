package prj.dee.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

	/**
	 * 写文件
	 * 
	 * @param filePath
	 * @param image
	 */
	public static void fileUpload(String filePath, File image) {
		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {
			File file = new File(filePath);
			if (!file.exists())
				file.createNewFile();
			// 建立文件输出流
			fos = new FileOutputStream(filePath);
			// 建立文件上传流
			fis = new FileInputStream(image);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 */
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists())
			file.delete();
	}

	/**
	 * 读取文件为String
	 * 
	 * @param src
	 * @return
	 */
	public static String readFromFile(File src) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(src));
			StringBuilder stringBuilder = new StringBuilder();
			String content;
			while ((content = bufferedReader.readLine()) != null) {
				stringBuilder.append(content);
			}
			return stringBuilder.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (bufferedReader != null)
					bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static boolean saveFile(MultipartFile file, String filePath) {
		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				// // 文件保存路径
				// String filePath = code + "upload/"
				// + file.getOriginalFilename();
				// 转存文件
				file.transferTo(new File(filePath));
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 保存MultipartFile
	 * @param file
	 * @param path
	 */
	public static void MultipartFileUpload(MultipartFile file, String path) {
		InputStream inStream = null;
		FileOutputStream outSteam = null;
		try {
			inStream = file.getInputStream();
			outSteam = new FileOutputStream(path);
			byte[] buffer = new byte[1024 * 1024];
			int bytesum = 0;
			int byteread = 0;
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				outSteam.write(buffer, 0, byteread);
				outSteam.flush();
			}
			outSteam.close();
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 递归查找文件
	 * 
	 * @param baseDirName
	 *            查找的文件夹路径
	 * @param targetFileName
	 *            需要查找的文件名
	 * @param fileList
	 *            查找到的文件集合
	 */
	public static void findFiles(String baseDirName, String targetFileName, List<String> fileList, Integer fileNum) {

		File baseDir = new File(baseDirName); // 创建一个File对象
		if (!baseDir.exists() || !baseDir.isDirectory()) { // 判断目录是否存在
			System.out.println("文件查找失败：" + baseDirName + "不是一个目录！");
		}
		String tempName = null;
		// 判断目录是否存在
		File tempFile;
		File[] files = baseDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			tempFile = files[i];
			if (tempFile.isDirectory()) {
				findFiles(tempFile.getAbsolutePath(), targetFileName, fileList, fileNum);
			} else if (tempFile.isFile()) {
				tempName = tempFile.getName();
				if (fileList.size() > fileNum) {
					break;
				}
				if (BaseUtil.wildcardMatch(targetFileName, tempName)) {
					// 匹配成功，将文件名添加到结果集
					fileList.add(tempFile.getAbsoluteFile().getPath());
				}
			}
		}
		return;
	}

}
