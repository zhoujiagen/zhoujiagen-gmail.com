package main

import (
	"bufio"
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"

	"github.com/kr/pretty"
	"github.com/open-policy-agent/opa/rego"
)

func main() {
	ctx := context.Background()

	// 创建rego对象
	r := rego.New(
		rego.Query("data.example.violation"),     // 查询
		rego.Load([]string{"example.rego"}, nil), // rego文件
	)

	// 创建查询对象
	query, err := r.PrepareForEval(ctx)
	if err != nil {
		log.Fatal(err)
	}

	// 加载数据
	var input interface{}
	inputJSONFile, err := os.Open("input.json")
	if err != nil {
		log.Fatal(err)
	}
	defer inputJSONFile.Close()

	dec := json.NewDecoder(bufio.NewReader(inputJSONFile))
	dec.UseNumber()
	if err := dec.Decode(&input); err != nil {
		log.Fatal(err)
	}

	// 执行查询, 获取结果
	rs, err := query.Eval(ctx, rego.EvalInput(input))
	if err != nil {
		log.Fatal(err)
	}

	fmt.Printf("%v\n", rs)
	pretty.Print(rs)
}
