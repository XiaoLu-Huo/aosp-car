import re
import os
import argparse

def process_java_file(file_path):
    with open(file_path, 'r+') as f:
        content = f.read()
        # 使用正则替换所有 FIELD_NUMBER 常量
        modified = re.sub(
            r'public static final int ([A-Z_]+)_FIELD_NUMBER = (\d+);',
            r'public static final int \1 = \2;',
            content
        )
        f.seek(0)
        f.write(modified)
        f.truncate()

def main():
    parser = argparse.ArgumentParser(description='Process Java proto files.')
    parser.add_argument('directory', help='Root directory to process Java files')
    args = parser.parse_args()

    # 遍历生成目录下的所有 Java 文件
    for root, dirs, files in os.walk(args.directory):
        for file in files:
            if file.endswith('.java'):
                process_java_file(os.path.join(root, file))

if __name__ == '__main__':
    main()