package com.ingka.sbp.di.poslogparse.xml;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

//import org.springframework.util.;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utilities for manipulating resource paths and URLs.
 *
 * @author Dave Syer
 * @author Phillip Webb
 * @since 1.0.0
 */
public abstract class ResourceUtilsOLD {

	/**
	 * Pseudo URL prefix for loading from the class path: "classpath:".
	 */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	/**
	 * Pseudo URL prefix for loading all resources from the class path: "classpath*:".
	 */
	public static final String ALL_CLASSPATH_URL_PREFIX = "classpath*:";

	/**
	 * URL prefix for loading from the file system: "file:".
	 */
	public static final String FILE_URL_PREFIX = "file:";
	
	
	/*private static InputStream resourceToInputStream(Resource resource) {
	    try {
	        return resource.getInputStream();
	    } catch (IOException e) {
	        throw propagate(e);
	    }
	}*/
	
	//17
	public static InputStream getResourceAsStream(String resource)
		      throws IOException {
		    ClassLoader cl = ResourceUtils.class.getClassLoader();
		    InputStream in = cl.getResourceAsStream(resource);

		    if (in == null) {
		      throw new IOException("resource \"" + resource + "\" not found");
		    }

		    return in;
		  }
	

	/**
	 * Return URLs from a given source path. Source paths can be simple file locations
	 * (/some/file.java) or wildcard patterns (/some/**). Additionally the prefixes
	 * "file:", "classpath:" and "classpath*:" can be used for specific path types.
	 * @param path the source path
	 * @param classLoader the class loader or {@code null} to use the default
	 * @return a list of URLs
	 */
	public static List<String> getUrls(String path, ClassLoader classLoader) {
		if (classLoader == null) {
			classLoader = ClassUtils.getDefaultClassLoader();
		}
		path = StringUtils.cleanPath(path);
		try {
			return getUrlsFromWildcardPath(path, classLoader);
		}
		catch (Exception ex) {
			throw new IllegalArgumentException("Cannot create URL from path [" + path + "]", ex);
		}
	}

	private static List<String> getUrlsFromWildcardPath(String path, ClassLoader classLoader) throws IOException {
		if (path.contains(":")) {
			return getUrlsFromPrefixedWildcardPath(path, classLoader);
		}
		Set<String> result = new LinkedHashSet<>();
		try {
			result.addAll(getUrls(FILE_URL_PREFIX + path, classLoader));
		}
		catch (IllegalArgumentException ex) {
			// ignore
		}
		path = stripLeadingSlashes(path);
		result.addAll(getUrls(ALL_CLASSPATH_URL_PREFIX + path, classLoader));
		return new ArrayList<>(result);
	}

	private static List<String> getUrlsFromPrefixedWildcardPath(String path, ClassLoader classLoader)
			throws IOException {
		Resource[] resources = new PathMatchingResourcePatternResolver(new FileSearchResourceLoader(classLoader))
				.getResources(path);
		List<String> result = new ArrayList<>();
		for (Resource resource : resources) {
			if (resource.exists()) {
				if ("file".equals(resource.getURI().getScheme()) && resource.getFile().isDirectory()) {
					result.addAll(getChildFiles(resource));
					continue;
				}
				result.add(absolutePath(resource));
			}
		}
		return result;
	}

	private static List<String> getChildFiles(Resource resource) throws IOException {
		Resource[] children = new PathMatchingResourcePatternResolver().getResources(resource.getURL() + "/**");
		List<String> childFiles = new ArrayList<>();
		for (Resource child : children) {
			if (!child.getFile().isDirectory()) {
				childFiles.add(absolutePath(child));
			}
		}
		return childFiles;
	}

	private static String absolutePath(Resource resource) throws IOException {
		if (!"file".equals(resource.getURI().getScheme())) {
			return resource.getURL().toExternalForm();
		}
		return resource.getFile().getAbsoluteFile().toURI().toString();
	}

	private static String stripLeadingSlashes(String path) {
		while (path.startsWith("/")) {
			path = path.substring(1);
		}
		return path;
	}

	private static class FileSearchResourceLoader extends DefaultResourceLoader {

		private final FileSystemResourceLoader files;

		FileSearchResourceLoader(ClassLoader classLoader) {
			super(classLoader);
			this.files = new FileSystemResourceLoader();
		}

		@Override
		public Resource getResource(String location) {
			Assert.notNull(location, "Location must not be null");
			if (location.startsWith(CLASSPATH_URL_PREFIX)) {
				return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()), getClassLoader());
			}
			if (location.startsWith(FILE_URL_PREFIX)) {
				return this.files.getResource(location);
			}
			try {
				// Try to parse the location as a URL...
				URL url = new URL(location);
				return new UrlResource(url);
			}
			catch (MalformedURLException ex) {
				// No URL -> resolve as resource path.
				return getResourceByPath(location);
			}
		}

	}

}
