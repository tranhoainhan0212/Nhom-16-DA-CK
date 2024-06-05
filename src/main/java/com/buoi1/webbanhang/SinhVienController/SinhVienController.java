package com.buoi1.webbanhang.SinhVienController;

import com.buoi1.webbanhang.model.SinhVien;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ClassPathResource;
import java.io.File;
import java.io.IOException;

@Controller
public class SinhVienController {

    @GetMapping("/sinhvien")
    public String showForm(Model model) {
        model.addAttribute("sinhVien", new SinhVien());
        return "sinhvien/form-sinhvien";
    }

    @PostMapping("/sinhvien")
    public String submitForm(@Valid SinhVien sinhVien, BindingResult bindingResult,
                             @RequestParam("image") MultipartFile image, Model model) {
        if (bindingResult.hasErrors()) {
            return "sinhvien/form-sinhvien";
        }


        if (!image.isEmpty()) {
            try {
                String uploadDirectory = new ClassPathResource("static/images/").getFile().getAbsolutePath() + File.separator;
                File uploadDir = new File(uploadDirectory);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String filePath = uploadDirectory + image.getOriginalFilename();
                image.transferTo(new File(filePath));
                sinhVien.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                return "sinhvien/form-sinhvien";
            }
        }

        model.addAttribute("sinhVien", sinhVien);
        model.addAttribute("message", "Sinh viên đã được thêm thành công!");
        return "sinhvien/result-sinhvien";
    }
}