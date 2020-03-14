package com.demo.espublico.salesapp.controller;

import com.demo.espublico.salesapp.entities.ISalesCount;
import com.demo.espublico.salesapp.forms.FormCsv;
import com.demo.espublico.salesapp.repositories.ISalesRepository;
import com.demo.espublico.salesapp.entities.SalesRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Controller
@Slf4j
@RequestMapping("/myapp")
public class SalesController {
    private ISalesRepository salesRepository;

    public SalesController(ISalesRepository ISalesRepository) {
        this.salesRepository = ISalesRepository;
    }

    @GetMapping("/index")
    public String showIndex(Model model) {
        model.addAttribute( "csvfile", new FormCsv());
        return "index";
    }

    @GetMapping("/plainSales")
    Collection<ISalesCount> sales() {
        Collection<ISalesCount> salesCount = salesRepository.countTotalByRegion();
        salesCount.addAll(salesRepository.countTotalByCountry());
        salesCount.addAll(salesRepository.countTotalByItemType());
        salesCount.addAll(salesRepository.countTotalBySalesChannel());
        salesCount.addAll(salesRepository.countTotalByOrderPriority());
        return salesCount;
    }

    @PostMapping("/sales")
    public String salesPost(
            @ModelAttribute("csvfile") FormCsv csvfile,
            // WARN: BindingResult *must* immediately follow the Command.
            // https://stackoverflow.com/a/29883178/1626026
            BindingResult bindingResult,
            Model model,
            RedirectAttributes ra ) {

        log.info("form submission.");

        if ( bindingResult.hasErrors() ) {
            return "index";
        }

        ra.addFlashAttribute("csvfile", csvfile);

        return "redirect:/myapp/stats";
    }

    @GetMapping("/stats")
    public String stats(
        @ModelAttribute("csvfile") FormCsv csvfile,
        Model model) {

        log.info( "!!!" + csvfile.toString());
        Path resourceDirectory = Paths.get("src","main","resources", "static");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        log.info("absolutePath " +absolutePath);

        System.out.println(absolutePath);
        salesRepository.deleteAll();

        Path source = Paths.get(absolutePath, csvfile.getTextField().concat(".csv"));
        log.info("source " +source);
        String lineText = null;
        try {
            BufferedReader lineReader = Files.newBufferedReader(source);
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                SalesRegistry salesRegister = new SalesRegistry();
                salesRegister.setRegion(data[0]);
                salesRegister.setCountry(data[1]);
                salesRegister.setItemType(data[2]);
                salesRegister.setSalesChannel(data[3].equalsIgnoreCase("Online"));
                salesRegister.setOrderPriority(data[4]);
                salesRegister.setOrderDate(LocalDate.parse(data[5], DateTimeFormatter.ofPattern( "M/d/uuuu")));
                salesRegister.setOrderId(Long.valueOf(data[6]));
                salesRegister.setShipDate(LocalDate.parse(data[7], DateTimeFormatter.ofPattern( "M/d/uuuu") ));
                salesRegister.setUnitsSold(Long.valueOf(data[8]));
                salesRegister.setUnitPrice(new BigDecimal(data[9]));
                salesRegister.setUnitCost(new BigDecimal(data[10]));
                salesRegister.setTotalRevenue(new BigDecimal(data[11]));
                salesRegister.setTotalCost(new BigDecimal(data[12]));
                salesRegister.setTotalProfit(new BigDecimal(data[13]));
                log.info("salesregister " +salesRegister);
                salesRepository.save(salesRegister);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return "index";
        }
        Collection<SalesRegistry> salesRegistry = salesRepository.findByOrderByOrderIdAsc();

        try {
            Path dest = Paths.get(absolutePath, "OrderedSalesRegistry.csv");
            StringBuilder datos = new StringBuilder();
            for (SalesRegistry s:salesRegistry){
                datos.append(s).append(System.getProperty("line.separator"));
            }
            byte[] strToBytes = datos.toString().getBytes();
            Files.write(dest, strToBytes);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return "index";
        }

        Collection<ISalesCount> regionStats = salesRepository.countTotalByRegion();
        Collection<ISalesCount> countryStats = salesRepository.countTotalByCountry();
        Collection<ISalesCount> itemStats = salesRepository.countTotalByItemType();
        Collection<ISalesCount> salesChannelStats = salesRepository.countTotalBySalesChannel();
        Collection<ISalesCount> orderPriorityStats = salesRepository.countTotalByOrderPriority();

        model.addAttribute( "regionStats", regionStats);
        model.addAttribute( "countryStats", countryStats);
        model.addAttribute( "itemStats", itemStats);
        model.addAttribute( "salesChannelStats", salesChannelStats);
        model.addAttribute( "orderPriorityStats", orderPriorityStats);

        return "stats";
    }

    @GetMapping("/load/{fileName}")
    Collection<ISalesCount> load(@PathVariable  String fileName) {

        Path resourceDirectory = Paths.get("src","main","resources", "static");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        log.info("absolutePath " +absolutePath);

        System.out.println(absolutePath);
        salesRepository.deleteAll();

        Path source = Paths.get(absolutePath, fileName.concat(".csv"));
        log.info("source " +source);
        String lineText = null;
        try {
            BufferedReader lineReader = Files.newBufferedReader(source);
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                SalesRegistry salesRegister = new SalesRegistry();
                salesRegister.setRegion(data[0]);
                salesRegister.setCountry(data[1]);
                salesRegister.setItemType(data[2]);
                salesRegister.setSalesChannel(data[3].equalsIgnoreCase("Online"));
                salesRegister.setOrderPriority(data[4]);
                salesRegister.setOrderDate(LocalDate.parse(data[5], DateTimeFormatter.ofPattern( "M/d/uuuu")));
                salesRegister.setOrderId(Long.valueOf(data[6]));
                salesRegister.setShipDate(LocalDate.parse(data[7], DateTimeFormatter.ofPattern( "M/d/uuuu") ));
                salesRegister.setUnitsSold(Long.valueOf(data[8]));
                salesRegister.setUnitPrice(new BigDecimal(data[9]));
                salesRegister.setUnitCost(new BigDecimal(data[10]));
                salesRegister.setTotalRevenue(new BigDecimal(data[11]));
                salesRegister.setTotalCost(new BigDecimal(data[12]));
                salesRegister.setTotalProfit(new BigDecimal(data[13]));
                log.info("salesregister " +salesRegister);
                salesRepository.save(salesRegister);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        Collection<SalesRegistry> salesRegistry = salesRepository.findByOrderByOrderIdAsc();

        try {
            Path dest = Paths.get(absolutePath, "OrderedSalesRegistry.csv");
            StringBuilder datos = new StringBuilder();
            for (SalesRegistry s:salesRegistry){
                datos.append(s).append(System.getProperty("line.separator"));
            }
            byte[] strToBytes = datos.toString().getBytes();
            Files.write(dest, strToBytes);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        Collection<ISalesCount> salesCount = salesRepository.countTotalByRegion();
        salesCount.addAll(salesRepository.countTotalByRegion());
        salesCount.addAll(salesRepository.countTotalByItemType());
        salesCount.addAll(salesRepository.countTotalBySalesChannel());
        salesCount.addAll(salesRepository.countTotalByOrderPriority());

        return salesCount;
    }


    @GetMapping("/clean")
    public ResponseEntity<?> delete() {
        log.info("Request to delete all");
        salesRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

}
