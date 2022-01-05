package com.securityteste.securityspringteste.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.securityteste.securityspringteste.exceptions.StorageException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceImpl implements StorageService{

	@Value("${upload.path}")
	private String uploadPath;

	private Path rootLocation;

	@Override
	@PostConstruct
	public void init() {
		try {
			this.rootLocation = Paths.get(this.uploadPath);

			Files.createDirectories(this.rootLocation);
		} catch (IOException e) {
			throw new StorageException("Não foi possível inicializar o local de armazenamento!");
		}
	}

	@Override
	public String store(MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		fileName = System.currentTimeMillis() + "-" + fileName.toLowerCase().replaceAll(" ", "-");

		if(file.isEmpty()){
			throw new StorageException("Falha ao armazenar arquivo vazio " + fileName);
		}

		if(fileName.contains("..")){
			throw new StorageException("Não é possível armazenar arquivo com caminho relativo para o diretório atual! " + fileName);
		}

		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, this.rootLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new StorageException("Falha ao salvar arquivo " + fileName, e);
		}

		return fileName;
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
							.filter(path -> !path.equals(this.rootLocation))
							.map(this.rootLocation::relativize);
		} catch (IOException e) {
			throw new StorageException("Falha ao ler arquivos armazenados!");
		}
	}

	@Override
	public Path load(String fileName) {
		return this.rootLocation.resolve(fileName);
	}

	@Override
	public Resource loadAsResource(String fileName) {
		try {
			Path file = load(fileName);
			Resource resource = new UrlResource(file.toUri());

			if(resource.exists() || resource.isReadable()){
				return resource;
			}
			else{
				throw new StorageException("Falha ao ler arquivo " + fileName);
			}
		} catch (MalformedURLException e) {
			throw new StorageException("Falha ao ler arquivo " + fileName);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(this.rootLocation.toFile());
	}

}
