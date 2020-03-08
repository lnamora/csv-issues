package com.demo.espublico.salesapp.web;

import com.demo.espublico.salesapp.model.ISalesCount;
import com.demo.espublico.salesapp.model.ISalesRepository;
import com.demo.espublico.salesapp.model.SalesRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/myapp")
public class SalesController {
    private ISalesRepository salesRepository;

    public SalesController(ISalesRepository ISalesRepository) {
        this.salesRepository = ISalesRepository;
    }

    @GetMapping("/sales")
    Collection<SalesRegistry> sales() {
        return salesRepository.findByOrderByOrderIdAsc();
    }

    @GetMapping("/statusRegion")
    List<ISalesCount> statusRegion() {
        log.info("Request to select statusRegion");
        List<ISalesCount> count = salesRepository.countTotalItemTypeByRegion();
        log.info("count "+count);
        return count;

    }
/*
    @GetMapping("/statusRegion")
    ResponseEntity<?>  statusRegion() {
        log.info("Request to select statusRegion");
        Optional<List<ISalesCount>> count = salesRepository.countTotalItemTypeByRegion();
        log.info("count "+count);
        return count.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
    */


    @GetMapping("/statusCountry")
    List<ISalesCount> statusCountry() {
        log.info("Request to select statusCountry");
        List<ISalesCount> count = salesRepository.countTotalItemTypeByCountry();
        log.info("count "+count);
        return count;

    }

    @GetMapping("/status/{stats}")
    List<ISalesCount> status(@PathVariable  String stats) {
        log.info("Request to select status: {}", stats);
        List<ISalesCount> count = null;
            switch (stats) {
                case "region":
                    log.info("Request to select status: {}", stats);
                    count = salesRepository.countTotalItemTypeByRegion();
                    break;
                case "country":
                    count = salesRepository.countTotalItemTypeByCountry();
                    break;
                default:
                    count = null;
                    break;
            }
        ;
        return count;
    }

    @GetMapping("/stats/{stats}")
    ResponseEntity<?> stats(@PathVariable  String stats) {
        log.info("Request to select stats: {}", stats);
        ResponseEntity<?> result = null;
        try {
            switch (stats) {
                case "region":
                    log.info("Request to select stats: {}", stats);
                    result = ResponseEntity.created(new URI("/myapp/statusRegion/" )).body(result);
                    break;
                case "country":
                    result = ResponseEntity.created(new URI("/myapp/statusCountry" )).body(result);
                    break;
                default:
                    result = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    break;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ;
        return result;

  //java version 13 preview
  //      var result = switch (grouping) {
  //          case "region" -> {return count.map(response -> ResponseEntity.ok().body(response))
   //                 .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); }
    //        default -> {return count.map(response -> ResponseEntity.ok().body(response))
   //                 .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); }
   //     };
    }

    @GetMapping("/load/{fileName}")
    Collection<ISalesCount> load(@PathVariable  String fileName) {

        Path resourceDirectory = Paths.get("src","main","resources", "static");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        log.info("absolutePath " +absolutePath);

        System.out.println(absolutePath);
        salesRepository.deleteAll();

        Path source = Paths.get(absolutePath, fileName);
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

        Collection<ISalesCount> salesCount = salesRepository.countTotalItemTypeByRegion();
        salesCount.addAll(salesRepository.countTotalItemTypeByRegion());
        salesCount.addAll(salesRepository.countTotalItemTypeByItemType());
        salesCount.addAll(salesRepository.countTotalItemTypeBySalesChannel());
        salesCount.addAll(salesRepository.countTotalItemTypeByOrderPriority());


        return salesCount;
    }


    @GetMapping("/clean")
    public ResponseEntity<?> delete() {
        log.info("Request to delete all");
        salesRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

}
