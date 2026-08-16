package com.example.cloudvault.controller;

import com.example.cloudvault.dto.ApiResp;
import com.example.cloudvault.folder.FileEntity;
import com.example.cloudvault.folder.FolderEntity;
import com.example.cloudvault.repository.FileRepository;
import com.example.cloudvault.service.FolderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class FolderController {

    private final FolderService folderService;
    public FolderController(FolderService todoService) {
        this.folderService = todoService;
    }

    @GetMapping("/getAllFiles")
    public ApiResp GetFolderData(@RequestParam long id){
        try{
            Object result = folderService.getFolderData(id);
            return new ApiResp("ok", result);
        }catch (Exception e){
            return new ApiResp("notOk", e.getMessage());
        }
    }

//Testing pending
    @PostMapping("/upload-file")
    public ApiResp UploadFile(@RequestParam long id,
                              @RequestParam("file") MultipartFile file,
                              String fPath){
        try{
            boolean saved = folderService.upload(file,id,fPath);
            if (saved){
                Object o =  folderService.saveMetaDataOnSuccess(file,id,fPath);
                return new ApiResp("ok", o);
            }
            throw new Exception("Save Failed");
        }catch (Exception e){
            return new ApiResp("notOk", e.getMessage());
        }
    }

    // testing pending
    @DeleteMapping("/deleteFile")
    public void deleteFile(@RequestParam long id) {
        try {
            folderService.deleteFile(id);
        }catch (Exception e){
            System.out.println(e);
        }
    }


    @DeleteMapping("/deleteFolder")
    public void deleteFolder(@RequestParam long id) {
        try {
            folderService.deleteFolder(id);
        }catch (Exception e) {
            System.out.println(e);
        }
    }


    @PostMapping("/createFolders")
    public ApiResp createFolder(
            @RequestParam String name,
            @RequestParam(required = false) Long parentId,
            HttpServletRequest request
    ) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            FolderEntity folder = folderService.createFolder(name, parentId,userId);

            return new ApiResp("ok", folder);

        } catch (Exception e) {
            return new ApiResp(
                    "notOk",
                    e.getClass().getName() + ": " + e.getMessage()
            );

        }
    }

    @GetMapping("/search")
    public ApiResp search(@RequestParam String name) {
        try {
            List<Object> result = folderService.search(name);

            return new ApiResp("ok", result);

        } catch (Exception e) {
            return new ApiResp("notOk", e.getMessage());
        }
    }
}
